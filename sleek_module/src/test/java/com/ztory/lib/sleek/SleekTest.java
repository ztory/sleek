package com.ztory.lib.sleek;

import com.ztory.lib.sleek.base.element.css.CSSblockBase;
import com.ztory.lib.sleek.mapd.Mapd;

import junit.framework.TestCase;

public class SleekTest extends TestCase {

    private static final String CSS_STRING_1 = "{\n" +
                                               "        background: #d8d8d8;\n" +
                                               "        color: #666;\n" +
                                               "        border-radius: 22px;\n" +
                                               "        margin: 0 0 8px 0;\n" +
                                               "        font-size: 10px;\n" +
                                               "        width: 46px;\n" +
                                               "        height: 46px;\n" +
                                               "        line-height: 46px;\n" +
                                               "        text-align: center;\n" +
                                               "    }";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCSSblockParseCSSstring() {
        CSSblockBase cssBlock = new CSSblockBase(CSS_STRING_1);
        //assertEquals(0xffd8d8d8, cssBlock.getBackgroundColor().intValue());
        assertEquals("#d8d8d8", Mapd.get(cssBlock, "background", String.class));
        assertEquals(22, cssBlock.getBorderRadius().intValue());
        assertEquals(10, cssBlock.getFontSize().intValue());
        assertEquals(46, cssBlock.getLineHeight().intValue());
    }

}
