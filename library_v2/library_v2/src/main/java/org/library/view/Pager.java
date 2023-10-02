package org.library.view;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.ui.Model;

/**
 * util for page
 * @param <T>
 */
@Getter
public class Pager<T> {
    protected static final int PAGE_SIZE = 5;

    private List<T> allItems;
    private List<T> pageItems;
    private int prevPage;
    private int nextPage;
    private int currPage;
    private Model model;

    public Pager(List<T> items, int currPage, Model model) {
        this.allItems = items;
        this.currPage = currPage;
        this.model = model;

        calculatePages();
    }

    @Override
    public String toString() {
        return "Pager{" +
                "allItems=" + allItems +
                ", pageItems=" + pageItems +
                ", prevPage=" + prevPage +
                ", nextPage=" + nextPage +
                ", currPage=" + currPage +
                '}';
    }

    /**
     * calculate the page and items
     */
    private void calculatePages() {
        if (allItems == null || allItems.isEmpty()) {
            prevPage = 1;
            currPage = 1;
            nextPage = 1;
            pageItems = new ArrayList<>();
        } else {
            int totalPage = (int)Math.ceil(allItems.size() / (float) PAGE_SIZE);
            currPage = Math.min(currPage, totalPage);
            if (currPage <= 0) {
                currPage = 1;
            }

            int startOffset = (currPage - 1) * PAGE_SIZE;
            int endOffset = Math.min(startOffset + PAGE_SIZE, allItems.size());
            pageItems = allItems.subList(startOffset, endOffset);
            prevPage = Math.max(1, currPage - 1);
            nextPage = Math.min(totalPage, currPage + 1);
        }

        model.addAttribute("pageItems", pageItems);
        model.addAttribute("pageCurr", currPage);
        model.addAttribute("pagePrev", prevPage);
        model.addAttribute("pageNext", nextPage);
    }
}
