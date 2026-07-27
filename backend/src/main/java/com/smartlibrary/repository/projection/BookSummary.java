package com.smartlibrary.repository.projection;

/**
 * Closed projection interface — Spring Data returns a lightweight view instead
 * of the full entity. Demonstrates interface-based projections.
 */
public interface BookSummary {
    Long getId();
    String getTitle();
    String getAuthor();
    String getIsbn();
    Integer getAvailableQuantity();
}
