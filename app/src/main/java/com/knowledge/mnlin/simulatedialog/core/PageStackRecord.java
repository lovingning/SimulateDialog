package com.knowledge.mnlin.simulatedialog.core;

import com.knowledge.mnlin.simulatedialog.interfaces.Page;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * Created on 2019/10/14  11:07
 * function : page-stack
 * <p>
 * for page route
 *
 * @author mnlin0905@gmail.com
 */
final class PageStackRecord {
    /**
     * record page
     */
    private LinkedList<Page> records = new LinkedList<>();

    /**
     * @param page {@link Page}
     */
    boolean removePage(@NotNull Page page) {
        return records.remove(page);
    }

    /**
     * insertPage record
     *
     * @param page  {@link Page}
     * @param index the index of page who required add
     */
    void insertPage(int index, @NotNull Page page) {
        records.add(index, page);
    }

    /**
     * @param index page's position in {@link records}
     * @return index corresponding to record
     */
    @Nullable
    Page findPage(int index) {
        if (records.size() > index) {
            return records.get(index);
        }
        return null;
    }

    /**
     * get all pages
     *
     * @return all pages
     */
    @NotNull
    LinkedList<Page> findAllPages() {
        return records;
    }
}
