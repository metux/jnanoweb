/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 - 2004 Greg Luck.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by Greg Luck
 *       (http://sourceforge.net/users/gregluck) and contributors.
 *       See http://jpam.sourceforge.net for a list of contributors"
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "jpam" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For written
 *    permission, please contact Greg Luck (gregluck at users.sourceforge.net).
 *
 * 5. Products derived from this software may not be called "jpam"
 *    nor may "jpam" appear in their names without prior written
 *    permission of Greg Luck.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL GREG LUCK OR OTHER
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by contributors
 * and individuals on behalf of the jpam project.  For more
 * information on jpam, please see <http://jpam.sourceforge.net/>.
 *
 * This file is derived from the Linux Pam project. The following copyright notice
 * also applies.
 *
 * Copyright Theodore Ts'o, 1996.  All rights reserved.
 * Copyright (c) Andrew G. Morgan <morgan@linux.kernel.org>, 1996-8
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, and the entire permission notice in its entirety,
 *    including the disclaimer of warranties.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote
 *    products derived from this software without specific prior
 *    written permission.
 *
 * ALTERNATIVELY, this product may be distributed under the terms of
 * the GNU Public License, in which case the provisions of the GPL are
 * required INSTEAD OF the above restrictions.  (This clause is
 * necessary due to a potential bad interaction between the GPL and
 * the restrictions contained in a BSD-style copyright.)
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package net.sf.jpam;

import java.util.List;
import java.util.Collections;
import java.util.Arrays;

/**
 * A type-safe enum for PAM return values.
 * <p/>
 * Warning. When comparing values do not use <code>==</code>.
 * Use the <code>.equals(Object o)</code> method.
 * <p/>
 * These are based on the Linux PAM projects return values.
 *
 * @author <a href="mailto:gregluck@users.sourceforge.net">Greg Luck</a>
 * @version $Id: PamReturnValue.java,v 1.4 2004/11/16 10:16:28 gregluck Exp $
 */
public class PamReturnValue {

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_SUCCESS =
	    new PamReturnValue(0, "Successful function return.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_OPEN_ERR =
	    new PamReturnValue(1, "dlopen() failure when dynamically loading a service module.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_SYMBOL_ERR =
	    new PamReturnValue(2, "Symbol not found.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_SERVICE_ERR =
	    new PamReturnValue(3, "Error in service module.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_SYSTEM_ERR =
	    new PamReturnValue(4, "System error.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_BUF_ERR =
	    new PamReturnValue(5, "Memory buffer error.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_PERM_DENIED =
	    new PamReturnValue(6, "Permission denied.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_AUTH_ERR =
	    new PamReturnValue(7, "Authentication failure.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_CRED_INSUFFICIENT =
	    new PamReturnValue(8, "Can not access authentication data due to insufficient credentials.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_AUTHINFO_UNAVAIL =
	    new PamReturnValue(9, "Underlying authentication service can not retrieve authentication information.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_USER_UNKNOWN =
	    new PamReturnValue(10, "User not known to the underlying authentication module.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_MAXTRIES =
	    new PamReturnValue(11, "An authentication service has maintained a retry "
	                       + "count which has been reached.  No further retries should be attempted.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_NEW_AUTHTOK_REQD =
	    new PamReturnValue(12, "New authentication token required. This is normally returned if"
	                       + " the machine security policies require that the password should be changed because"
	                       + " the password is NULL or it has aged.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_ACCT_EXPIRED =
	    new PamReturnValue(13, "User account has expired.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_SESSION_ERR =
	    new PamReturnValue(14, "Can not make/remove an entry for the specified session.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_CRED_UNAVAIL =
	    new PamReturnValue(15, "Underlying authentication service can not retrieve user credentials unavailable.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_CRED_EXPIRED =
	    new PamReturnValue(16, "User credentials expired.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_CRED_ERR =
	    new PamReturnValue(17, "Failure setting user credentials.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_NO_MODULE_DATA =
	    new PamReturnValue(18, "No module specific data is present.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_CONV_ERR =
	    new PamReturnValue(19, "Conversation error.");
	/**
	 * A constant PamReturnValue
	 */

	public static final PamReturnValue PAM_AUTHTOK_ERR =
	    new PamReturnValue(20, "Authentication token manipulation error.");
	/**
	 * A constant PamReturnValue
	 */

	public static final PamReturnValue PAM_AUTHTOK_RECOVER_ERR =
	    new PamReturnValue(21, "Authentication information cannot be recovered.");
	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_AUTHTOK_LOCK_BUSY =
	    new PamReturnValue(22, "Authentication token lock busy.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_AUTHTOK_DISABLE_AGING =
	    new PamReturnValue(23, "Authentication token aging disabled.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_TRY_AGAIN =
	    new PamReturnValue(24, "Preliminary check by password service.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_IGNORE =
	    new PamReturnValue(25, "Ignore underlying account module regardless of whether the control flag"
	                       + "is required, optional, or sufficient.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_ABORT =
	    new PamReturnValue(26, "Critical error (?module fail now request).");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_AUTHTOK_EXPIRED =
	    new PamReturnValue(27, "User's authentication token has expired.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_MODULE_UNKNOWN =
	    new PamReturnValue(28, "Module is not known.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_BAD_ITEM =
	    new PamReturnValue(29, "Bad item passed to pam_*_item().");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_CONV_AGAIN =
	    new PamReturnValue(30, "Conversation function is event driven and data is not available yet.");

	/**
	 * A constant PamReturnValue
	 */
	public static final PamReturnValue PAM_INCOMPLETE =
	    new PamReturnValue(31, "Please call this function again to complete authentication stack. Before calling"
	                       + " again, verify that conversation is completed.");

	private static final PamReturnValue[] PRIVATE_VALUES = {
		PAM_SUCCESS, PAM_OPEN_ERR, PAM_SYMBOL_ERR, PAM_SERVICE_ERR, PAM_SYSTEM_ERR,
		PAM_BUF_ERR, PAM_PERM_DENIED, PAM_AUTH_ERR, PAM_CRED_INSUFFICIENT, PAM_AUTHINFO_UNAVAIL,
		PAM_USER_UNKNOWN, PAM_MAXTRIES, PAM_NEW_AUTHTOK_REQD, PAM_ACCT_EXPIRED, PAM_SESSION_ERR,
		PAM_CRED_UNAVAIL, PAM_CRED_EXPIRED, PAM_CRED_ERR, PAM_NO_MODULE_DATA, PAM_CONV_ERR,
		PAM_AUTHTOK_ERR, PAM_AUTHTOK_RECOVER_ERR, PAM_AUTHTOK_LOCK_BUSY, PAM_AUTHTOK_DISABLE_AGING,
		PAM_TRY_AGAIN, PAM_IGNORE, PAM_ABORT, PAM_AUTHTOK_EXPIRED, PAM_MODULE_UNKNOWN, PAM_BAD_ITEM,
		PAM_CONV_AGAIN, PAM_INCOMPLETE
	};

	private final String description;

	private final int id;

	private PamReturnValue(int id, String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * Returns true if the supplied object is of the
	 * same type and has the same id.
	 */
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PamReturnValue)) {
			return false;
		}
		final PamReturnValue pamReturnValue = (PamReturnValue) o;
		if (id != pamReturnValue.id) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the PamReturnValue that matches the given id
	 * @param id a valid Integer with a value between 0 and 31
	 * @return the PamReturnValue matching the id
	 * @throws IllegalArgumentException if the id is outside the range of possible return values
	 */
	public static PamReturnValue fromId(int id) throws IllegalArgumentException {
		int maxId = VALUES.size() - 1;
		if (id > maxId || id < 0) {
			throw new IllegalArgumentException("id " + id + " is not between 0 and " + maxId);
		}
		return (PamReturnValue) VALUES.get(id);
	}

	/**
	 * @return a hash code for the object.
	 */
	public int hashCode() {
		return id;
	}

	/**
	 * @return the String description of the return value
	 */
	public String toString() {
		return description;
	}

	/**
	 * The enumeration of possible values
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));
}
