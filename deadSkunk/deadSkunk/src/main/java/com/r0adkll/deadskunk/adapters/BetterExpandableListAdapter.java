
/*
 * MIT License (MIT)
 *
 * Copyright (c) 2014 Drew Heavner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.r0adkll.deadskunk.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * My Improved ExpandableListAdapter that implements the ViewHolder
 * pattern for efficient rendering
 *
 * @param <H>   The class type for the header data
 * @param <C>   The class type for the child data
 */
public abstract class BetterExpandableListAdapter<H,C> extends BaseExpandableListAdapter{

    /**********************************************************
     *
     * Variables
     *
     */

    private Context mCtx;
    private LayoutInflater mInflater;

    private int mHeaderViewResId;
    private int mChildViewResId;

    private List<H> mHeaders;
    private Map<H, List<C>> mChildren;

    /**
     * Constructor
     *
     * @param ctx                   the application context
     * @param headerViewResId       the resource id for the header view
     * @param childViewResId        the resource id for the child view
     * @param headers               the list of header data
     * @param children              the map of children data
     */
    public BetterExpandableListAdapter(Context ctx, int headerViewResId, int childViewResId,
                                       List<H> headers, Map<H, List<C>> children){

        mCtx = ctx;
        mInflater = LayoutInflater.from(mCtx);
        mHeaderViewResId = headerViewResId;
        mChildViewResId = childViewResId;
        mHeaders = headers;
        mChildren = children;

    }

    public Context getContext(){
        return mCtx;
    }

    public Resources getResources(){
        return mCtx.getResources();
    }

    /**********************************************************
     *
     * BaseExpandableListAdapter methods
     *
     */

    @Override
    public int getGroupCount() {
        return mHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildren.get(mHeaders.get(groupPosition)).size();
    }

    @Override
    public H getGroup(int groupPosition) {
        return mHeaders.get(groupPosition);
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        return mChildren.get(mHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition | childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder holder;

        if(convertView == null){
            // Inflate new header view
            convertView = mInflater.inflate(mHeaderViewResId, parent, false);

            // Create View HOlder
            holder = createParentViewHolder(convertView);

            // Set the holder as the tag
            convertView.setTag(holder);
        }else{
            holder = (ParentViewHolder) convertView.getTag();
        }

        bindParentHolder(holder, groupPosition, isExpanded);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;

        if(convertView == null){

            convertView = mInflater.inflate(mChildViewResId, parent, false);
            holder = createChildViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder = (ChildViewHolder) convertView.getTag();
        }

        bindChildHolder(holder, groupPosition, childPosition, isLastChild);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**********************************************************
     *
     * ViewHolder Objects
     *
     */

    public abstract ParentViewHolder createParentViewHolder(View parentView);
    public abstract ChildViewHolder createChildViewHolder(View childView);

    public abstract void bindParentHolder(ParentViewHolder holder, int groupPosition, boolean isExpanded);
    public abstract void bindChildHolder(ChildViewHolder holder, int groupPosition, int childPosition, boolean isLastChild);

    public static class ParentViewHolder{}
    public static class ChildViewHolder{}
}
