package com.megadict.format.dict.index.segment;

/**
 * This class is a companion class of {@code SegmentIndexer} classes. It just a mere
 * data structure used to store information when performing creating segments.
 */

class Block {

    Block(String headword, int offset) {
        this.headword = headword;
        this.offset = offset;
    }

    final String headword;
    int offset;
}
