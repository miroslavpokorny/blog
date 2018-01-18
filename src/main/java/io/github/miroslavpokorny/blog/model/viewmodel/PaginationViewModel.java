package io.github.miroslavpokorny.blog.model.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginationViewModel extends BaseViewModel {
    private static final int MAX_SHOW_PAGES = 5;

    Integer page;

    Integer numberOfPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public boolean isGoToFirstPageEnabled() {
        return page != 1;
    }

    public boolean isGoToLastPageEnabled() {
        return !page.equals(numberOfPages);
    }

    public List<Integer> getPages (){
        int maxPages = Math.min(MAX_SHOW_PAGES, numberOfPages);
        int rightOffset = (int) Math.ceil(maxPages / 2.0);
        int leftOffset = (int) Math.floor(maxPages / 2.0);
        while (page + rightOffset > numberOfPages) {
            leftOffset++;
            rightOffset--;
        }
        while (page - leftOffset < 1) {
            rightOffset++;
            leftOffset--;
        }
        int left = page - leftOffset;
        int right = page + rightOffset;
        List<Integer> result = new ArrayList<Integer>();
        for (int p : IntStream.range(left, right).toArray()) {
            result.add(p);
        }
        return result;
    }
}
