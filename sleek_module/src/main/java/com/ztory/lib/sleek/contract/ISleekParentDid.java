package com.ztory.lib.sleek.contract;

import com.ztory.lib.sleek.SleekCanvas;
import com.ztory.lib.sleek.SleekParent;

/**
 * Interface that is called with SleekCanvas and SleekParent, can be used as callback when Sleek
 * is added or removed from its parent.
 * Created by jonruna on 09/10/14.
 */
public interface ISleekParentDid {
    void parentDid(SleekCanvas sleekCanvas, SleekParent composite);
}
