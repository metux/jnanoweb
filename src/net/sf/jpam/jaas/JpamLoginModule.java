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
 */

package net.sf.jpam.jaas;

import net.sf.jpam.Pam;
import net.sf.jpam.PamReturnValue;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

/**
 * A <code>LoginModule</code> which invokes JPAM. This can be used from standard
 * JAAS implementations. It is an alternative to directly using the {@link Pam} class.
 * <p/>
 * This class relies on the existence of a .java.login.config with a login configuration
 * called <code>net-sf-jpam</code>. Copy the .java.login.config in the src/config/<architecture>
 * directory to running user's home directory.
 * @author <a href="mailto:gregluck@users.sourceforge.net">Greg Luck</a>
 * @version $Id: JpamLoginModule.java,v 1.5 2005/06/18 19:18:37 gregluck Exp $
 */
public class JpamLoginModule implements LoginModule {
	private static final String SERVICE_NAME_OPTION = "serviceName";
	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map sharedState;
	private Map options;

	/**
	 * Method to abort the authentication process (phase 2).
	 * <p/>
	 * <p> This method is called if the LoginContext's
	 * overall authentication failed.
	 * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
	 * did not succeed).
	 * <p/>
	 * <p> If this LoginModule's own authentication attempt
	 * succeeded (checked by retrieving the private state saved by the
	 * <code>login</code> method), then this method cleans up any state
	 * that was originally saved.
	 * <p/>
	 * <p/>
	 *
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * @throws javax.security.auth.login.LoginException
	 *          if the abort fails
	 */
	public boolean abort() throws LoginException {
		return true;
	}

	/**
	 * Method to commit the authentication process (phase 2).
	 * <p/>
	 * <p> This method is called if the LoginContext's
	 * overall authentication succeeded
	 * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
	 * succeeded).
	 * <p/>
	 * <p> If this LoginModule's own authentication attempt
	 * succeeded (checked by retrieving the private state saved by the
	 * <code>login</code> method), then this method associates relevant
	 * Principals and Credentials with the <code>Subject</code> located in the
	 * <code>LoginModule</code>.  If this LoginModule's own
	 * authentication attempted failed, then this method removes/destroys
	 * any state that was originally saved.
	 * <p/>
	 * <p/>
	 *
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * @throws javax.security.auth.login.LoginException
	 *          if the commit fails
	 */
	public boolean commit() throws LoginException {
		return true;
	}

	/**
	 * Method to authenticate a <code>Subject</code> (phase 1).
	 * <p/>
	 * <p> The implementation of this method authenticates
	 * a <code>Subject</code>.  For example, it may prompt for
	 * <code>Subject</code> information such
	 * as a username and password and then attempt to verify the password.
	 * This method saves the result of the authentication attempt
	 * as private state within the LoginModule.
	 * <p/>
	 * <p/>
	 *
	 * @return true if the authentication succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * @throws javax.security.auth.login.LoginException
	 *          if the authentication fails
	 */
	public boolean login() throws LoginException {
		Pam pam = createPam();

		Callback[] callbacks = new Callback[2];
		String username = null;
		NameCallback nameCallback = new NameCallback("Enter Username: ");
		callbacks[0] = nameCallback;
		String credentials = null;
		PasswordCallback passwordCallback = new PasswordCallback("Enter Credentials: ", false);
		callbacks[1] = passwordCallback;

		try {
			callbackHandler.handle(callbacks);
		} catch (IOException e) {
			throw new LoginException(e.getMessage());
		} catch (UnsupportedCallbackException e) {
			throw new LoginException(e.getMessage());
		}
		username = nameCallback.getName();
		credentials = String.copyValueOf(passwordCallback.getPassword());
		boolean authenticated = false;
		PamReturnValue pamReturnValue = pam.authenticate(username, credentials);
		if (pamReturnValue.equals(PamReturnValue.PAM_SUCCESS)) {
			authenticated = true;
		} else if (pamReturnValue.equals(PamReturnValue.PAM_ACCT_EXPIRED)) {
			throw new AccountExpiredException(PamReturnValue.PAM_ACCT_EXPIRED.toString());
		} else if (pamReturnValue.equals(PamReturnValue.PAM_CRED_EXPIRED)) {
			throw new CredentialExpiredException(PamReturnValue.PAM_CRED_EXPIRED.toString());
		} else {
			throw new FailedLoginException(pamReturnValue.toString());
		}
		return authenticated;
	}

	private Pam createPam() {
		String serviceName = (String)options.get(SERVICE_NAME_OPTION);
		if (serviceName == null) {
			System.out.println("No serviceName configured in JAAS configuration file. Using default service name of "
			                   + Pam.DEFAULT_SERVICE_NAME);
			serviceName = Pam.DEFAULT_SERVICE_NAME;
		} else {
			System.out.println("Using service name of "
			                   + serviceName + " from JAAS configuration file");
			serviceName = Pam.DEFAULT_SERVICE_NAME;
		}
		Pam pam = new Pam(serviceName);
		return pam;
	}

	/**
	 * Method which logs out a <code>Subject</code>.
	 * <p/>
	 * <p>An implementation of this method might remove/destroy a Subject's
	 * Principals and Credentials.
	 * <p/>
	 * <p/>
	 *
	 * @return true if this method succeeded, or false if this
	 *         <code>LoginModule</code> should be ignored.
	 * @throws javax.security.auth.login.LoginException
	 *          if the logout fails
	 */
	public boolean logout() throws LoginException {
		return true;
	}

	/**
	 * Initialize this LoginModule.
	 * <p/>
	 * <p> This method is called by the <code>LoginContext</code>
	 * after this <code>LoginModule</code> has been instantiated.
	 * The purpose of this method is to initialize this
	 * <code>LoginModule</code> with the relevant information.
	 * If this <code>LoginModule</code> does not understand
	 * any of the data stored in <code>sharedState</code> or
	 * <code>options</code> parameters, they can be ignored.
	 * <p/>
	 * <p/>
	 *
	 * @param subject         the <code>Subject</code> to be authenticated. <p>
	 * @param callbackHandler a <code>CallbackHandler</code> for communicating
	 *                        with the end user (prompting for usernames and
	 *                        passwords, for example). <p>
	 * @param sharedState     state shared with other configured LoginModules. <p>
	 * @param options         options specified in the login
	 *                        <code>Configuration</code> for this particular
	 *                        <code>LoginModule</code>.
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
	}
}
