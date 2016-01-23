package org.sqlite;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * Compile-time authorization callback
 *
 * @see Conn#setAuhtorizer(Authorizer, Pointer)
 * @see <a href="http://sqlite.org/c3ref/set_authorizer.html">sqlite3_set_authorizer</a>
 */
public interface Authorizer extends Callback {
	/**
	 * @param pArg       User data ({@link Conn#setAuhtorizer(Authorizer, Pointer)} second parameter)
	 * @param actionCode {@link ActionCodes}.*
	 * @return {@link #SQLITE_OK} or {@link #SQLITE_DENY} or {@link #SQLITE_IGNORE}
	 */
	int invoke(Pointer pArg, int actionCode, String arg1, String arg2, String dbName, String triggerName);

	int SQLITE_OK = ErrCodes.SQLITE_OK;
	/**
	 * @see <a href="http://sqlite.org/c3ref/c_deny.html">Authorizer Return Codes</a>
	 */
	int SQLITE_DENY = 1;
	/**
	 * @see <a href="http://sqlite.org/c3ref/c_deny.html">Authorizer Return Codes</a>
	 */
	int SQLITE_IGNORE = 2;
}