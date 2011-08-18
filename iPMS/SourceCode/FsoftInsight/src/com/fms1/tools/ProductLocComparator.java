/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 /*
 * Created on Aug 21, 2007
 * @author trungtn
 *
 */
package com.fms1.tools;

import java.util.Comparator;

import com.fms1.infoclass.ProductLocInfo;
import com.fms1.infoclass.WPSizeInfo;

/**
 * Comparators that will be used for sort lists in Product LOC listing page
 * @author trungtn
 *
 */
public class ProductLocComparator {
    /**
     * The comparator that will be used for sort Product LOC listing by
     * language. The input data should be ProductLocInfo instances
     * @author trungtn
     *
     */
    public static class LocByLanguage implements Comparator {
        // Singleton instance
        private static LocByLanguage locByLanguage = null;

        // Prevent from create instance directly, should use getInstance() method 
        private LocByLanguage() {
        }
        /**
         * Get an instance (singleton)
         * @return
         */
        public static LocByLanguage getInstance() {
            if (locByLanguage == null) {
                locByLanguage = new LocByLanguage();
            }
            return locByLanguage;
        }

        public int compare(Object loc1, Object loc2) {
            String language1 = ((ProductLocInfo)loc1).getLanguageName();
            String language2 = ((ProductLocInfo)loc2).getLanguageName();
            return language1.compareToIgnoreCase(language2);
        }
    }

    /**
     * The comparator that will be used for sort Product LOC listing by
     * product. The input data should be WPSizeInfo instances
     * @author trungtn
     *
     */
    public static class LocByProduct implements Comparator {
        // Singleton instance
        private static LocByProduct locByProduct = null;
        
        // Prevent from create instance directly, should use getInstance() method 
        private LocByProduct() {
        }
        /**
         * Get an instance (singleton)
         * @return
         */
        public static LocByProduct getInstance() {
            if (locByProduct == null) {
                locByProduct = new LocByProduct();
            }
            return locByProduct;
        }

        public int compare(Object loc1, Object loc2) {
            String wp1 = ((WPSizeInfo)loc1).categoryName;
            String wp2 = ((WPSizeInfo)loc2).categoryName;
            String product1 = ((WPSizeInfo)loc1).name;
            String product2 = ((WPSizeInfo)loc2).name;
            // Sort by Work product then by Product name.
            if (wp1.equalsIgnoreCase(wp2)) {
                return product1.compareToIgnoreCase(product2);
            }
            else {
                return wp1.compareToIgnoreCase(wp2);
            }
        }
    }

}
