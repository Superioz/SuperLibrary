package de.superioz.library.bukkit.common.session;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import de.superioz.library.java.util.Consumer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * UUIDFetcher class
 * @author evilmidget38
 */
public class UUIDFetcher implements Callable<Map<String, UUID>> {

	private static final double PROFILES_PER_REQUEST = 100;
	private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
	private final JSONParser jsonParser = new JSONParser();
	private final List<String> names;
	private final boolean rateLimiting;

	public UUIDFetcher(List<String> names, boolean rateLimiting){
		this.names = ImmutableList.copyOf(names);
		this.rateLimiting = rateLimiting;
	}

	public UUIDFetcher(List<String> names){
		this(names, true);
	}

	/**
	 * Get uuid map from connection
	 *
	 * @return The map
	 * @throws Exception
	 */
	public Map<String, UUID> call() throws Exception{
		Map<String, UUID> uuidMap = new HashMap<>();
		int requests = (int) Math.ceil(names.size() / PROFILES_PER_REQUEST);
		for(int i = 0; i < requests; i++){
			HttpURLConnection connection = createConnection();
			String body = JSONArray.toJSONString(names.subList(i * 100, Math.min((i + 1) * 100, names.size())));
			writeBody(connection, body);
			JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
			for(Object profile : array){
				JSONObject jsonProfile = (JSONObject) profile;
				String id = (String) jsonProfile.get("id");
				String name = (String) jsonProfile.get("name");
				UUID uuid = UUIDFetcher.getUUID(id);
				uuidMap.put(name, uuid);
			}
			if(rateLimiting && i != requests - 1){
				Thread.sleep(100L);
			}
		}
		return uuidMap;
	}

	/**
	 * Writes body of string to connecton
	 *
	 * @param connection The connection
	 * @param body       The string body
	 * @throws Exception
	 */
	private static void writeBody(HttpURLConnection connection, String body) throws Exception{
		OutputStream stream = connection.getOutputStream();
		stream.write(body.getBytes());
		stream.flush();
		stream.close();
	}

	/**
	 * Creates connection to uuid url
	 *
	 * @return The connection
	 * @throws Exception
	 */
	private static HttpURLConnection createConnection() throws Exception{
		URL url = new URL(PROFILE_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		return connection;
	}

	/**
	 * Get the uuid of this id
	 *
	 * @param id The id
	 * @return The uuid
	 */
	private static UUID getUUID(String id){
		return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
	}

	/**
	 * UUID to bytes
	 *
	 * @param uuid The uuid
	 * @return The byte array
	 */
	public static byte[] toBytes(UUID uuid){
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());
		return byteBuffer.array();
	}

	/**
	 * UUID from bytes
	 *
	 * @param array The array
	 * @return The uuid
	 */
	public static UUID fromBytes(byte[] array){
		if(array.length != 16){
			throw new IllegalArgumentException("Illegal byte array length: " + array.length);
		}
		ByteBuffer byteBuffer = ByteBuffer.wrap(array);
		long mostSignificant = byteBuffer.getLong();
		long leastSignificant = byteBuffer.getLong();
		return new UUID(mostSignificant, leastSignificant);
	}

	/**
	 * Get uuid of given name
	 *
	 * @param name The name
	 * @return The uuid
	 * @throws Exception
	 */
	public static UUID getUUIDOf(String name) throws Exception{
		return new UUIDFetcher(Collections.singletonList(name)).call().get(name);
	}
}
