package com.epam_final_project.storage;

import java.util.Map;

public interface DataLoaderStrategy<T> {
    T loadData(String[] csvData);
    void storeData(T entity, Map<Integer, T> storage);
}
