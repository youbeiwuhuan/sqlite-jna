package org.sqlite;

import org.sqlite.SQLite.SQLite3Context;
import org.sqlite.SQLite.SQLite3Values;

import static org.sqlite.SQLite.sqlite3_aggregate_context;
import static org.sqlite.SQLite.sqlite3_result_error_nomem;

/**
 * User defined SQL aggregate function.
 * <pre>{@code
 * new AggregateStepCallback() {
 *   \@Override
 *   public void step(SQLite3Context pCtx, Object aggrCtx, SQLite3Values args) {
 *     args.getX(...);
 *     ...
 *     aggrCtx.setX(...);
 *   }
 * }
 * }</pre>
 *
 * @see Conn#createAggregateFunction(String, int, int, AggregateStepCallback, AggregateFinalCallback)
 * @see <a href="http://sqlite.org/c3ref/create_function.html">sqlite3_create_function_v2</a>
 */
public abstract class AggregateStepCallback<A> {
	//void (*)(sqlite3_context*,int,sqlite3_value**),
	/**
	 * @param pCtx <code>sqlite3_context*</code>
	 * @param args function arguments
	 */
	@SuppressWarnings("unused")
	public void callback(long pCtx, long[] args) {
		final A p = (A) sqlite3_aggregate_context(pCtx, 1);
		if (p == null) {
			sqlite3_result_error_nomem(pCtx);
			return;
		}
		step(new SQLite3Context(pCtx), p, SQLite3Values.build(args));
	}

	/**
	 * @return aggregate contexte
	 * @see <a href="http://sqlite.org/c3ref/aggregate_context.html">sqlite3_aggregate_context</a>
	 */
	protected abstract A createAggregateContext();

	/**
	 * @param pCtx <code>sqlite3_context*</code>
	 * @param aggrCtx aggregate context
	 * @param args function arguments
	 */
	protected abstract void step(SQLite3Context pCtx, A aggrCtx, SQLite3Values args);
}