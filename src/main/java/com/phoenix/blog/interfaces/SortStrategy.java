package com.phoenix.blog.interfaces;

import java.util.List;

public interface SortStrategy{

    public void sortByUpvoteCount(List<Object> list);
    public void sortByReadCount(List<Object> list);

}
