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

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.account.manager.model.Account;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.safframework.log.L;
import com.task.cn.ConstantKt;
import com.task.cn.StatusCode;
import com.task.cn.jbean.TaskBean;
import com.task.cn.manager.TaskManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 21/09/2017.
 */

public class TableViewListener implements ITableViewListener {
    @NonNull
    private Context mContext;
    @NonNull
    private TableView mTableView;
    @NonNull
    private TableViewModel mTableViewModel;


    private Account mAccount = null;

    public TableViewListener(@NonNull TableView tableView, @NonNull TableViewModel tableViewModel) {
        this.mContext = tableView.getContext();
        this.mTableView = tableView;
        this.mTableViewModel = tableViewModel;
    }

    /**
     * Called when user click any cell item.
     *
     * @param cellView : Clicked Cell ViewHolder.
     * @param column   : X (Column) position of Clicked Cell item.
     * @param row      : Y (Row) position of Clicked Cell item.
     */
    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

        // Do what you want.
        //showToast("Cell " + column + " " + row + " has been clicked.");

    }

    /**
     * Called when user long press any cell item.
     *
     * @param cellView : Long Pressed Cell ViewHolder.
     * @param column   : X (Column) position of Long Pressed Cell item.
     * @param row      : Y (Row) position of Long Pressed Cell item.
     */
    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, final int column,
                                  int row) {
        // Do What you want
        //showToast("Cell " + column + " " + row + " has been long pressed.");
    }

    /**
     * Called when user click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param column           : X (Column) position of Clicked Column Header item.
     */
    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {
        // Do what you want.
        //showToast("Column header  " + column + " has been clicked.");
    }


    /**
     * Called when user long press any column header item.
     *
     * @param columnHeaderView : Long Pressed Column Header ViewHolder.
     * @param column           : X (Column) position of Long Pressed Column Header item.
     */
    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {

       /* if (columnHeaderView instanceof ColumnHeaderViewHolder) {
            // Create Long Press Popup
            ColumnHeaderLongPressPopup popup = new ColumnHeaderLongPressPopup(
                    (ColumnHeaderViewHolder) columnHeaderView, mTableView);
            // Show
            popup.show();
        }*/
    }

    /**
     * Called when user click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param row           : Y (Row) position of Clicked Row Header item.
     */
    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        L.d("row: " + row);
        if (mAccount != null) {
            showToast("正在读取账号(" + mAccount.getAccount() + ")改机");
            return;
        }
        Account account = mTableViewModel.getAccountList().get(row);
        mAccount = account;
        showToast("正在读取账号(" + account.getAccount() + ")改机");

        TaskBean taskBean = new TaskBean();
        taskBean.setDevice_info(account.getDeviceInfoBean());
        List<String> platformList = new ArrayList<>();
        platformList.add(ConstantKt.getPlatformPkgByInt(Integer.valueOf(account.getPlatform())));

        new TaskManager.Companion.TaskBuilder()
                .setIpSwitch(true)
                .setTaskInfoSwitch(true)
                .setTaskBean(taskBean)
                .setCityCode(account.getLogin_info().getCity_code())
                .setPlatformList(platformList)
                .setTaskControllerView(result -> {
                    if (result.getCode() == StatusCode.SUCCEED) {
                        showToast("改机成功，正在打开应用");
                        String platformPkg = ConstantKt.getPlatformPkgByInt(Integer.valueOf(account.getPlatform()));
                        L.d("改机成功，正在打开应用: " + platformPkg);
                        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(platformPkg);
                        if (launchIntent == null) {
                            showToast("未安装" + ConstantKt.getPlatformNameByInt(Integer.valueOf(account.getPlatform())));
                        } else {
                            mContext.startActivity(launchIntent);
                        }
                    } else {
                        L.d("改机失败：" + result.getMsg());
                        showToast(result.getMsg());
                    }
                    mAccount = null;
                })
                .build()
                .startTask();
    }


    /**
     * Called when user long press any row header item.
     *
     * @param rowHeaderView : Long Pressed Row Header ViewHolder.
     * @param row           : Y (Row) position of Long Pressed Row Header item.
     */
    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        // Create Long Press Popup
        //RowHeaderLongPressPopup popup = new RowHeaderLongPressPopup(rowHeaderView, mTableView);
        // Show
        //popup.show();
    }


    private void showToast(String p_strMessage) {
        Toast.makeText(mContext, p_strMessage, Toast.LENGTH_SHORT).show();
    }
}
