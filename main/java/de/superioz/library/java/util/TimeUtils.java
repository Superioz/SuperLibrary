package de.superioz.library.java.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class created on April in 2015
 */
public class TimeUtils {

    /**
     * Returns an integer array with following content:
     * [0] = hour
     * [1] = minute
     * [2] = second
     *
     * @return The integer array
     */
    public static int[] getTime(){
        int[] time = new int[3];
        LocalTime timeNow = LocalTime.now();

        // Put into array
        time[0] = timeNow.getHour();
        time[1] = timeNow.getMinute();
        time[2] = timeNow.getSecond();

        return time;
    }

    /**
     * Returns the year in an integer
     */
    public static int getYear(){
        LocalDate date = LocalDate.now();
        return date.getYear();
    }

    /**
     * Returns the current time in a string
     *
     * @return The string
     */
    public static String getCurrentTime(){
        int[] now = getTime();
        String[] nowString = new String[now.length];

        for(int i = 0; i < now.length; i++){
            String s =now[i]+"";

            if(s.length() == 1){
                s = "0"+now[i];
            }

            nowString[i] = s;
        }

        return nowString[0]+":"+nowString[1]+":"+nowString[2];
    }

    /**
     * Get current time / date with format
     */
    public static String getCurrentTime(String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date resultdate = new Date();
        return sdf.format(resultdate);
    }

    /**
     * Get current time / date with format
     */
    public static String getCurrentTime(long timstamp, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date resultdate = new Date(timstamp);
        return sdf.format(resultdate);
    }

    /**
     * Get current timestamp
     * @return Timestamp as a long
     */
    public static long timestamp(){
        return System.currentTimeMillis();
    }

    /**
     * @see #timestamp()
     */
    public static String timestamp(boolean shorten){
        if(shorten){
            return shortenTimestamp(timestamp());
        }
        else{
            return timestamp()+"";
        }
    }

    /**
     * @see #timestamp()
     */
    public static long timestamp(TimestampModifier timestampModifier){
        return timestamp() / timestampModifier.value();
    }

    /**
     * @see #timestamp()
     */
    public static String timestamp(TimestampModifier timestampModifier, boolean shorten){
        if(shorten){
            return shortenTimestamp(timestamp(timestampModifier));
        }
        else{
            return timestamp(timestampModifier)+"";
        }
    }

    /**
     * Get date from timestamp
     */
    public static String fromTimestamp(long timestamp, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date resultdate = new Date(timestamp);
        return sdf.format(resultdate);
    }

    /**
     * Replaces alphabetic chars with numbers
     */
    public static long fromShortenedTimestamp(String shortenedTimestamp){
        String[] alph = AlphabetUtils.ALPHABET.split("");
        String timestamp = shortenedTimestamp;

        for(String ch : alph){
            timestamp = timestamp.replaceAll(ch, AlphabetUtils.getFromChar(ch)+"");
        }

        return Long.valueOf(timestamp);
    }

    /**
     * Shorten the timestamp with numbers
     *
     * @param timestamp The original timestamp
     * @return The new timestamp string
     */
    public static String shortenTimestamp(long timestamp){
        String ts = timestamp+"";
        String[] tsArray = ts.split("");

        /*
        Take packets from timestamp (3er)
         */
        List<String> parts = new ArrayList<>();
        for(int i = 2; i < tsArray.length; i+=3){
            parts.add(tsArray[i-2] + tsArray[i-1] + tsArray[i]);
        }
        parts.add(tsArray[tsArray.length - 1]);

        /*
        Replace all numbers which can be replaced and create new list
         */
        List<String> newStamp = new ArrayList<>();
        for(String s : parts){
            if(s.length() == 3){
                String first = s.subSequence(0, 2)+"";
                String last = s.subSequence(1, 3)+"";
                int firstNum = Integer.valueOf(first);
                int lastNum = Integer.valueOf(last);

                if(firstNum > 0 && firstNum <= 52){
                    first = AlphabetUtils.getFromNumber(firstNum);
                    last = s.charAt(s.length()-1) + "";
                }
                else if(lastNum > 0 && lastNum <= 52){
                    last = AlphabetUtils.getFromNumber(lastNum);
                    first = s.charAt(s.length()-1) + "";
                }
                else{
                    first = AlphabetUtils.getFromNumber(Integer.valueOf(s.charAt(0) + ""));
                }

                newStamp.add(first + last);
            }
            else{
                newStamp.add(s);
            }
        }

        return newStamp.toString()
                .replaceAll(",","").replaceAll("\\[","").replaceAll("\\]","").replaceAll(" ", "");
    }

    /**
     * Converts time from ticks to seconds
     */
    public static int convertTime(int ticks, boolean toSeconds){
        return ticks * (toSeconds ? 20 : 1);
    }

    /**
     * Converts time from ticks to seconds
     */
    public static int convertTime(int ticks){
        return convertTime(ticks, true);
    }

    /**
     * Modifier for getting current time
     */
    public enum TimestampModifier {

        MILLISECONDS(1),
        SECONDS(MILLISECONDS.value() * 1000),
        MINUTES(SECONDS.value() * 60),
        HOURS(MINUTES.value() * 60),
        DAYS(HOURS.value() * 24),
        YEARS(DAYS.value() * 365);

        private long divide;

        TimestampModifier(long divide){
            this.divide = divide;
        }

        public long value(){
            return divide;
        }
    }

}
