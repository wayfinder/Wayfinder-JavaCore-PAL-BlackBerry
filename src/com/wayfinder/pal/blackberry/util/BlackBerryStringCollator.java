/*******************************************************************************
 * Copyright (c) 1999-2010, Vodafone Group Services
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 *     * Redistributions of source code must retain the above copyright 
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above 
 *       copyright notice, this list of conditions and the following 
 *       disclaimer in the documentation and/or other materials provided 
 *       with the distribution.
 *     * Neither the name of Vodafone Group Services nor the names of its 
 *       contributors may be used to endorse or promote products derived 
 *       from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE.
 ******************************************************************************/
package com.wayfinder.pal.blackberry.util;

import javax.microedition.global.StringComparator;

import net.rim.device.api.i18n.Locale;

import com.wayfinder.pal.util.StringCollator;

public class BlackBerryStringCollator implements StringCollator {
    
    private static BlackBerryStringCollator INSTANCE = null;
    
    private Locale m_locale;
    private StringComparator m_comparator;
    
    /**
     * Obtains a {@link StringCollator} set-up to use the default {@link Locale}
     * of the system for string comparisons
     * 
     * @param collationStrength the comparison level, one of 
     * {@link StringCollator#STRENGTH_PRIMARY}, 
     * {@link StringCollator#STRENGTH_SECONDARY}, or
     * {@link StringCollator#STRENGTH_TERTIARY}
     * 
     * @return a {@link StringCollator}
     */
    public static BlackBerryStringCollator get(int collationStrength) {
        return get(Locale.getDefault(), collationStrength);
    }
    
    /**
     * Obtains a {@link StringCollator} set-up to use the specified {@link Locale} for
     * string comparisons.
     * 
     * @param loc the {@link Locale}
     * @param collationStrength the comparison level, one of 
     * {@link StringCollator#STRENGTH_PRIMARY}, 
     * {@link StringCollator#STRENGTH_SECONDARY}, or
     * {@link StringCollator#STRENGTH_TERTIARY}
     * 
     * @return a {@link StringCollator}
     */
    public static BlackBerryStringCollator get(Locale locale, int collationStrength) {
        if (INSTANCE == null
                || !INSTANCE.m_locale.equals(locale)
                || INSTANCE.m_comparator.getLevel() != mapCollationStrength(collationStrength)) {
            INSTANCE = new BlackBerryStringCollator(locale, collationStrength);
        }
        return INSTANCE;
    }
    
    private BlackBerryStringCollator(Locale locale, int collationSregth) {
        m_locale = locale;
        m_comparator = new StringComparator(locale.getLanguage(), mapCollationStrength(collationSregth));
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.util.StringCollator#compare(java.lang.String, java.lang.String)
     */
    public int compare(String str1, String str2) {
        return m_comparator.compare(str1, str2);
    }
    
    /**
     * Maps the strength values from the Core PAL interface to the ones in 
     * {@link StringComparator}. 
     * The default level is {@link StringComparator#LEVEL2}.
     * 
     * @param palValue the value from the PAL interface
     * @return the corresponding strength constant from {@link StringComparator}
     */
    private static int mapCollationStrength(int palValue) {
        switch (palValue) {
        case StringCollator.STRENGTH_PRIMARY:
            return StringComparator.LEVEL1;
            
        case StringCollator.STRENGTH_SECONDARY:
            return StringComparator.LEVEL2;
            
        case StringCollator.STRENGTH_TERTIARY:
            return StringComparator.LEVEL3;
            
        default:
            return StringComparator.LEVEL2;
        }
    }
}
