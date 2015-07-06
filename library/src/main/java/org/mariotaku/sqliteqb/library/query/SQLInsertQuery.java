/*
 * Copyright (c) 2015 mariotaku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mariotaku.sqliteqb.library.query;

import org.mariotaku.sqliteqb.library.OnConflict;
import org.mariotaku.sqliteqb.library.SQLQuery;
import org.mariotaku.sqliteqb.library.Utils;

public class SQLInsertQuery implements SQLQuery {

    private OnConflict onConflict;
    private String table;
    private String[] columns;
    private String values;

    SQLInsertQuery() {

    }

    @Override
    public String getSQL() {
        if (table == null) throw new NullPointerException("table must not be null!");
        final StringBuilder sb = new StringBuilder();
        sb.append("INSERT ");
        if (onConflict != null) {
            sb.append("OR ");
            sb.append(onConflict.getAction());
            sb.append(" ");
        }
        sb.append("INTO ");
        sb.append(table);
        sb.append(" (");
        sb.append(Utils.toString(columns, ',', true));
        sb.append(") ");
        sb.append(values);
        return sb.toString();
    }

    void setColumns(final String[] columns) {
        this.columns = columns;
    }

    void setOnConflict(final OnConflict onConflict) {
        this.onConflict = onConflict;
    }

    void setSelect(final SQLSelectQuery select) {
        this.values = select.getSQL();
    }

    void setValues(final String... values) {
        this.values = "VALUES (" + Utils.toString(values, ',', true) + ")";
    }

    void setTable(final String table) {
        this.table = table;
    }

    public static final class Builder implements IBuilder<SQLInsertQuery> {

        private final SQLInsertQuery query = new SQLInsertQuery();

        private boolean buildCalled;

        @Override
        public SQLInsertQuery build() {
            buildCalled = true;
            return query;
        }

        @Override
        public String buildSQL() {
            return build().getSQL();
        }

        public Builder columns(final String[] columns) {
            checkNotBuilt();
            query.setColumns(columns);
            return this;
        }

        public Builder values(final String[] values) {
            checkNotBuilt();
            query.setValues(values);
            return this;
        }

        public Builder values(final String values) {
            checkNotBuilt();
            query.setValues(values);
            return this;
        }

        public Builder insertInto(final OnConflict onConflict, final String table) {
            checkNotBuilt();
            query.setOnConflict(onConflict);
            query.setTable(table);
            return this;
        }

        public Builder insertInto(final String table) {
            return insertInto(null, table);
        }

        public Builder select(final SQLSelectQuery select) {
            checkNotBuilt();
            query.setSelect(select);
            return this;
        }

        private void checkNotBuilt() {
            if (buildCalled) throw new IllegalStateException();
        }

    }

}
