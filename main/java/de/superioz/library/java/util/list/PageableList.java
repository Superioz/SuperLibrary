package de.superioz.library.java.util.list;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class PageableList<T> {

    protected List<T> objects;
    protected int objectsPerPage;
    protected int totalPages;

    public PageableList(int objectsPerPage, List<T> objects){
        this.objects = objects;
        this.objectsPerPage = objectsPerPage;

        this.totalPages = objects.size() / objectsPerPage;
        if(objects.size() % objectsPerPage != 0) this.totalPages++;
    }

    public List<T> calculatePage(int page){
        if(!firstCheckPage(page))
            return null;

        List<T> l = new ArrayList<>();
        int fromIndex = (page - 1) * objectsPerPage;
        int toIndex = fromIndex + objectsPerPage;

        for(int i = fromIndex; i < toIndex; i++){
            if(i > this.objects.size()-1){
                l.add(null);
                continue;
            }

            l.add(this.objects.get(i));
        }

        return l;
    }

    public List<T> nextPage(int page){
        return calculatePage(++page);
    }

    public boolean firstCheckPage(int page){
        return !(getTotalPages() == 0 || page > totalPages || page <= 0);
    }

    public int getObjectsPerPage(){
        return objectsPerPage;
    }

    public int getTotalPages(){
        return totalPages;
    }
}
