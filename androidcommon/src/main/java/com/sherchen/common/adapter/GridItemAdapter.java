/*
 * Copyright (C) 2014 Changchao project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sherchen.common.adapter;

import android.content.Context;
import android.view.View;


import java.util.List;

/**
 * The description of use:可以一行显示两个的adapter
 * <br />
 * Created time:2016/1/7 19:17
 * Created by Dave
 */
public abstract class GridItemAdapter<Entity, ViewHolder> extends ItemAdapter<Entity, ViewHolder> {
    public GridItemAdapter() {
    }

    public GridItemAdapter(Context context, int paramInt) {
        super(context, paramInt);
    }

    public GridItemAdapter(Context context, int layoutResId, List<Entity> list) {
        super(context, layoutResId, list);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        int quotinent = count / 2;
        int reminder = count % 2 == 0 ? quotinent : quotinent + 1;
        return reminder;
    }

    private int getSize(){
        return datalist == null ? 0 : datalist.size();
    }

    @Override
    public void setViewContent(ViewHolder viewHolder, Entity entity, View convertView, int position) {
        int firstPos = getFirstPos(position);
        int secondPos = getSecondPos(position);
        int size = getSize();
        Entity first = firstPos < size ? getItem(firstPos) : null;
        Entity second = secondPos < size ? getItem(secondPos) : null;
        setViewContent(viewHolder, first, second, convertView, position);
    }

    protected int getFirstPos(int position){
        return position == 0 ? 0 : position * 2 + 0;
    }

    protected int getSecondPos(int position){
        return position == 0 ? 1 : position * 2 + 1;
    }

    protected abstract void setViewContent(ViewHolder viewHolder, Entity first, Entity second, View convertView, int position);
}
