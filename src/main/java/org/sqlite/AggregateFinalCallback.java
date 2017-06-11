package org.sqlite;

import org.sqlite.SQLite.SQLite3Context;

import static org.sqlite.SQLite.sqlite3_aggregate_context;

/**
 * User defined SQL aggregate function.
 * <pre>{@code
 * new AggregateFinalCallback() {
 *   \@Override
 *   public void finalStep(SQLite3Context pCtx, Object aggrCtx) {
 *     if (aggrCtx == null) {
 *       pCtx.setResultNull();
 *       return;
 *     }
 *     ...
 *     pCtx.setResultX(...);
 *   }
 * }
 * }</pre>
 *
 * @see Conn#createAggregateFunction(String, int, int, AggregateStepCallback, AggregateFinalCallback)
 * @see <a href="http://sqlite.org/c3ref/create_function.html">sqlite3_create_function_v2</a>
 */
public abstract class AggregateFinalCallback<A> {
	/**
	 * @param pCtx <code>sqlite3_context*</code>
	 */
	@SuppressWarnings("unused")
	public void callback(long pCtx) {
		try {
			finalStep(new SQLite3Context(pCtx), getAggregateContext(pCtx));
		} finally {
			sqlite3_aggregate_context(pCtx, -1); // free
		}
	}

	protected abstract void finalStep(SQLite3Context pCtx, A aggrCtx);

	/**
	 * Obtain aggregate function context.
	 *
	 * @param pCtx <code>sqlite3_context*</code>
	 * @return <code>null</code> when no rows match an aggregate query.
	 * @see <a href="http://sqlite.org/c3ref/aggregate_context.html">sqlite3_aggregate_context</a>
	 */
	protected A getAggregateContext(long pCtx) {
		// Within the xFinal callback, it is customary to set N=0 in calls to sqlite3_aggregate_context(C,N)
		// so that no pointless memory allocations occur.
		return (A) sqlite3_aggregate_context(pCtx, 0);
	}
}