package com.bnb.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.annotation.MenuRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;
import android.view.MotionEvent;

import com.bnb.entity.Menu;
import com.bnb.library.R;

import org.w3c.dom.Attr;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：Administrator
 * 创建时间： 2016/7/18 0018 14:19
 * 功能概述:
 * 修改人：
 * 修改时间：
 */
public class MenuInflater {

    private static final String TAG = MenuInflater.class.getSimpleName();

    private Context mContext;

    /**
     * Menu tag name in XML.
     */
    private static final String XML_MENU = "menu";

    /**
     * Group tag name in XML.
     */
    private static final String XML_GROUP = "group";

    /**
     * Item tag name in XML.
     */
    private static final String XML_ITEM = "item";

    private List<Menu> menus;

    public MenuInflater(Context context) {
        this.mContext = context;
        this.menus = new ArrayList<Menu>();
    }


    public void inflater(int menuResId) {
        XmlResourceParser parser = null;
        try {
            parser = mContext.getResources().getLayout(menuResId);
            AttributeSet attrs = Xml.asAttributeSet(parser);

            parseMenu(parser, attrs);
        } catch (XmlPullParserException e) {
            throw new InflateException("Error inflating menu XML", e);
        } catch (IOException e) {
            throw new InflateException("Error inflating menu XML", e);
        } finally {
            if (parser != null) parser.close();
        }
    }

    private void parseMenu(XmlResourceParser mParser, AttributeSet attrs)
            throws XmlPullParserException, IOException {
        MenuState mMenuState = new MenuState();
        int eventType = mParser.getEventType();
        String tagName;
        boolean lookingForEndOfUnknownTag = false;
        String unknownTagName = null;
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = mParser.getName();
                if (tagName.equals(XML_MENU)) {
                    eventType = mParser.next();
                    break;
                }

                throw new RuntimeException("Expecting menu, got " + tagName);
            }
            eventType = mParser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);

        boolean reachedEndOfMenu = false;
        while (!reachedEndOfMenu) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (lookingForEndOfUnknownTag) {
                        break;
                    }
                    tagName = mParser.getName();
                    if (tagName.equals(XML_GROUP)) { //节点是group
                        //mMenuState.readGroup(attrs);
                        //do nothing
                    } else if (tagName.equals(XML_ITEM)) { //节点是item
                        mMenuState.readItem(attrs);
                    } else { //这里不需要在对有menu节点的进行解析，因为不会用到.
                        lookingForEndOfUnknownTag = true;
                        unknownTagName = tagName;
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = mParser.getName();
                    if (lookingForEndOfUnknownTag && tagName.equals(unknownTagName)) {
                        lookingForEndOfUnknownTag = false;
                        unknownTagName = null;
                    } else if (tagName.equals(XML_GROUP)) {
                        mMenuState.resetGroupId();
                    } else if (tagName.equals(XML_ITEM)) {
                        mMenuState.setItem();
                    } else if (tagName.equals(XML_MENU)) {
                        reachedEndOfMenu = true;
                    }
                    break;

                case XmlPullParser.END_DOCUMENT:
                    throw new RuntimeException("Unexpected end of document");
            }
            eventType = mParser.next();
        }

    }

    public List<Menu> getMenus() {
        return menus;
    }

    private class MenuState {

        private Menu menu;

        private int mGroupId;

        private static final int DEFAULT_GROUP_ID = 0;

        private int mItemId;

        private static final int DEFAULT_ITEM_ID = 0;

        private CharSequence mItemName;

        private int mItemIconId;

        private static final int DEFAULT_ITEM_ICON_ID = 0;

        public MenuState() {

        }

        public void resetGroupId() {
            this.mGroupId = DEFAULT_GROUP_ID;
        }

        /*public void readGroup(AttributeSet attrs) {
            TypedArray mArray = mContext.obtainStyledAttributes(attrs, R.styleable.MenuGroup);
            for (int i = 0; i < mArray.length(); i++) {
                int attr = mArray.getIndex(i);
                //Log.i(TAG, String.valueOf(attr));
                mArray.getResourceId(attr, DEFAULT_GROUP_ID);
            }
            mArray.recycle();
        }*/

        public void readItem(AttributeSet attrs) {
            TypedArray mArray = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItem);
            mItemId = mArray.getResourceId(getInternalResource("MenuItem_id"), DEFAULT_ITEM_ID);
            mItemName = mArray.getText(getInternalResource("MenuItem_title"));
            mItemIconId = mArray.getResourceId(getInternalResource("MenuItem_icon"), DEFAULT_ITEM_ICON_ID);
            mArray.recycle();
        }

        public void setItem() {
            Menu menu = new Menu();
            menu.setName(mItemName);
            menu.setRedId(mItemIconId);
            Log.i(TAG, menu.toString());
            menus.add(menu);
        }

        /**
         * 通过反射机制去获取变量
         * @param tag
         * @return
         */
        private int getInternalResource(String tag) {
            int id = 0;
            try{
                Class c = Class.forName("com.android.internal.R$styleable");
                Object obj = c.newInstance();
                Field field = c.getField(tag);
                id = field.getInt(obj);
            }catch(Exception e){
            }
            return id;
        }

    }


}
