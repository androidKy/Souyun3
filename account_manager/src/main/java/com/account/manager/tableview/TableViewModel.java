/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.account.manager.tableview;

import com.account.manager.model.Account;
import com.account.manager.tableview.model.Cell;
import com.account.manager.tableview.model.ColumnHeader;
import com.account.manager.tableview.model.RowHeader;

import java.util.List;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {

    public TableViewModel() {
    }

    public List<RowHeader> getRowHeaderList() {
        return mRowHeaderList;
    }

    public void setRowHeaderList(List<RowHeader> mRowHeaderList) {
        this.mRowHeaderList = mRowHeaderList;
    }

    public List<ColumnHeader> getColumnHeaderList() {
        return mColumnHeaderList;
    }

    public void setColumnHeaderList(List<ColumnHeader> mColumnHeaderList) {
        this.mColumnHeaderList = mColumnHeaderList;
    }

    public List<List<Cell>> getCellList() {
        return mCellList;
    }

    public void setCellList(List<List<Cell>> mCellList) {
        this.mCellList = mCellList;
    }


    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;

    private List<Account> mAccountList;

    public List<Account> getAccountList() {
        return mAccountList;
    }

    public void setAccountList(List<Account> mAccountList) {
        this.mAccountList = mAccountList;
    }
}
