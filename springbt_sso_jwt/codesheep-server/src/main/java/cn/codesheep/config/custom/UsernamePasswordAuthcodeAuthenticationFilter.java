package cn.codesheep.config.custom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

public class UsernamePasswordAuthcodeAuthenticationFilter extends
AbstractAuthenticationProcessingFilter {
// ~ Static fields/initializers
// =====================================================================================

public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
public static final String SPRING_SECURITY_FORM_AUTHCODE_KEY = "authcode";

private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
private String authcodeParameter = SPRING_SECURITY_FORM_AUTHCODE_KEY;
private boolean postOnly = true;

// ~ Constructors
// ===================================================================================================

public UsernamePasswordAuthcodeAuthenticationFilter() {
super(new AntPathRequestMatcher("/rubin/login", "POST"));
}

// ~ Methods
// ========================================================================================================

public Authentication attemptAuthentication(HttpServletRequest request,
	HttpServletResponse response) throws AuthenticationException {
if (postOnly && !request.getMethod().equals("POST")) {
	throw new AuthenticationServiceException(
			"Authentication method not supported: " + request.getMethod());
}

String username = obtainUsername(request);
String password = obtainPassword(request);
String authcode = obtainAuthcode(request);

if (username == null) {
	username = "";
}

if (password == null) {
	password = "";
}

if (authcode == null) {
	authcode = "";
}
username = username.trim();

UsernamePasswordAuthcodeAuthenticationToken authRequest = new UsernamePasswordAuthcodeAuthenticationToken(
		username, password,authcode);

// Allow subclasses to set the "details" property
setDetails(request, authRequest);

return this.getAuthenticationManager().authenticate(authRequest);
}

/**
* Enables subclasses to override the composition of the password, such as by
* including additional values and a separator.
* <p>
* This might be used for example if a postcode/zipcode was required in addition to
* the password. A delimiter such as a pipe (|) should be used to separate the
* password and extended value(s). The <code>AuthenticationDao</code> will need to
* generate the expected password in a corresponding manner.
* </p>
*
* @param request so that request attributes can be retrieved
*
* @return the password that will be presented in the <code>Authentication</code>
* request token to the <code>AuthenticationManager</code>
*/
protected String obtainPassword(HttpServletRequest request) {
return request.getParameter(passwordParameter);
}

/**
* Enables subclasses to override the composition of the username, such as by
* including additional values and a separator.
*
* @param request so that request attributes can be retrieved
*
* @return the username that will be presented in the <code>Authentication</code>
* request token to the <code>AuthenticationManager</code>
*/
protected String obtainUsername(HttpServletRequest request) {
return request.getParameter(usernameParameter);
}

protected String obtainAuthcode(HttpServletRequest request) {
return request.getParameter(authcodeParameter);
}

/**
* Provided so that subclasses may configure what is put into the authentication
* request's details property.
*
* @param request that an authentication request is being created for
* @param authRequest the authentication request object that should have its details
* set
*/
protected void setDetails(HttpServletRequest request,
	UsernamePasswordAuthcodeAuthenticationToken authRequest) {
authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
}

/**
* Sets the parameter name which will be used to obtain the username from the login
* request.
*
* @param usernameParameter the parameter name. Defaults to "username".
*/
public void setUsernameParameter(String usernameParameter) {
Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
this.usernameParameter = usernameParameter;
}

/**
* Sets the parameter name which will be used to obtain the password from the login
* request..
*
* @param passwordParameter the parameter name. Defaults to "password".
*/
public void setPasswordParameter(String passwordParameter) {
Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
this.passwordParameter = passwordParameter;
}


public void setAuthcodeParameter(String authcodeParameter) {
Assert.hasText(authcodeParameter, "authcode parameter must not be empty or null");
this.authcodeParameter = authcodeParameter;
}

/**
* Defines whether only HTTP POST requests will be allowed by this filter. If set to
* true, and an authentication request is received which is not a POST request, an
* exception will be raised immediately and authentication will not be attempted. The
* <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
* authentication.
* <p>
* Defaults to <tt>true</tt> but may be overridden by subclasses.
*/
public void setPostOnly(boolean postOnly) {
this.postOnly = postOnly;
}

public final String getUsernameParameter() {
return usernameParameter;
}

public final String getPasswordParameter() {
return passwordParameter;
}

public final String getAuthcodeParameter() {
return authcodeParameter;
}
}
