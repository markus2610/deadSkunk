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

package com.r0adkll.deadskunk.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.LruCache;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a Roboto font convenience helper class to easily apply
 * roboto fonts to textviews.
 *
 * To simply apply a typeface to a text view call {@link #applyTypeface(android.content.Context, android.widget.TextView, String)}
 * and use the easy string mapping to specify which roboto font you want to apply:
 * <pre>
 *     'roboto-regular'
 *     'roboto-bold'
 *     'roboto-italic'
 *     'roboto-medium'
 *     'roboto-thin'
 *     'roboto-light'
 *     'roboto-black'
 *     'roboto-black-italic'
 *     'roboto-bold-italic'
 *     'roboto-light-italic'
 *     'roboto-medium-italic'
 *     'roboto-thin-italic'
 *
 *     CONDENSED:
 *
 *     'robotocondensed-bold'
 *     'robotocondensed-italic'
 *     'robotocondensed-light'
 *     'robotocondensed-regular'
 *     'robotocondensed-bold-italic'
 *     'robotocondensed-light-italic'
 * </pre>
 *
 *
 * Created by drew.heavner on 3/26/14.
 */
public class FontLoader {

    private static final String ROBOTO = "Roboto";
    private static final String CONDENSED = "RobotoCondensed";

    private static final String BLACK = "Black";
    private static final String ITALIC = "Italic";
    private static final String BOLD = "Bold";
    private static final String LIGHT = "Light";
    private static final String MEDIUM = "Medium";
    private static final String THIN = "Thin";
    private static final String REGULAR = "Regular";

    private static final String[] TYPE_CHOICES = new String[]{ROBOTO, CONDENSED};

    /**
     * These are all the parts that can be followed by 'Italic'
     */
    private static final String[] TYPE_ROBOTO_CHOICES = new String[]{BLACK.toLowerCase(), BOLD.toLowerCase(), ITALIC.toLowerCase(), LIGHT.toLowerCase(), MEDIUM.toLowerCase(), REGULAR.toLowerCase(), THIN.toLowerCase()};
    private static final String[] TYPE_ROBOTO_FIRST_CHOICES = new String[]{BLACK.toLowerCase(), BOLD.toLowerCase(), LIGHT.toLowerCase(), MEDIUM.toLowerCase(), THIN.toLowerCase()};
    private static final String[] TYPE_CONDENSED_CHOICES = new String[]{BOLD.toLowerCase(), LIGHT.toLowerCase(), ITALIC.toLowerCase(), REGULAR.toLowerCase()};
    private static final String[] TYPE_CONDENSED_FIRST_CHOICES = new String[]{BOLD.toLowerCase(), LIGHT.toLowerCase()};

    /* Typeface memory cache */
    private static LruCache<String, Typeface> mLoadedTypefaces = new LruCache<>(2 * 1024 * 1024); // 2 MiB cache

    /**
     * Apply a typeface to a textview
     *
     * type = i.e. (roboto-black, roboto-black-italic, roboto-medium, condensed-light-italic)
     *
     * @param ctx           the application context
     * @param textView      the text view you wish to apply to
     * @param type          the typeface to apply
     */
    public static void applyTypeface(Context ctx, TextView textView, String type){
        // First check for existing typefaces
        Typeface typeface = getTypeface(ctx, type);
        if (typeface != null)
            textView.setTypeface(typeface);
    }

    /**
     * Apply a typeface to a given view by id
     *
     * @param ctx       the application context
     * @param viewId    the TextView to apply to id
     * @param parent    the parent of the text view
     * @param type      the typeface type argument
     */
    public static void applyTypeface(Context ctx, int viewId, View parent, String type){
        TextView text = (TextView) parent.findViewById(viewId);
        if(text != null){
            applyTypeface(ctx, text, type);
        }
    }

    /**
     * Get a Roboto typeface for a given string type
     *
     * @param ctx       the application context
     * @param type      the typeface type argument
     * @return          the loaded typeface, or null
     */
    public static Typeface getTypeface(Context ctx, String type){
        // Check for existing typefaces
        Typeface existing = mLoadedTypefaces.get(type);
        if(existing == null) {
            String typeface = parseTypefaceType(type);
            if (typeface != null && !typeface.isEmpty()) {
                existing = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + typeface);
            }
        }

        return existing;
    }

    /**
     * Parse the typeface type request parameter
     * to find the appropriate typeface file
     *
     * @param type      the typeface type argument
     * @return          the full typeface filename
     */
    private static String parseTypefaceType(String type){
        String[] parts = type.split("-");

        // Check the first part
        String builder = "";
        if(parts[0].equalsIgnoreCase(ROBOTO)){
            builder = ROBOTO.concat("-");
            builder = builder.concat(parseArguments(ROBOTO, Arrays.copyOfRange(parts, 1, parts.length)));
        }else if(parts[0].equalsIgnoreCase(CONDENSED)){
            builder = CONDENSED.concat("-");
            builder = builder.concat(parseArguments(CONDENSED, Arrays.copyOfRange(parts, 1, parts.length)));
        }

        // Concat the suffix of the font file
        builder = builder.concat(".ttf");

        return builder;
    }

    /**
     * Parse the type arguments for requesting a typeface
     *
     * @param type      the type, Roboto, or Condensed
     * @param args      the array of argument strings
     * @return          the parsed argument suffix string
     */
    private static String parseArguments(String type, String[] args){

        List<String> builder = new ArrayList<>();
        List<String> robotoChoices = Arrays.asList(TYPE_ROBOTO_CHOICES);
        List<String> robotoFirstChoices = Arrays.asList(TYPE_ROBOTO_FIRST_CHOICES);
        List<String> condensedChoices = Arrays.asList(TYPE_CONDENSED_CHOICES);
        List<String> condensedFirstChoices = Arrays.asList(TYPE_CONDENSED_FIRST_CHOICES);

        // Now analyze the rest of the parts
        int N = (args.length > 2) ? 2 : args.length;
        for(int i=0; i<N; i++){
            String part = args[i];

            if(type.equalsIgnoreCase(ROBOTO)){
                if(builder.size() == 0) {
                    if (robotoChoices.contains(part.toLowerCase())) {
                        // So the first
                        String id = (Character.toUpperCase(part.charAt(0))) + part.toLowerCase().substring(1);
                        switch (id){
                            case BLACK:
                                builder.add(BLACK);
                                break;
                            case BOLD:
                                builder.add(BOLD);
                                break;
                            case ITALIC:
                                builder.add(ITALIC);
                                break;
                            case LIGHT:
                                builder.add(LIGHT);
                                break;
                            case MEDIUM:
                                builder.add(MEDIUM);
                                break;
                            case REGULAR:
                                builder.add(REGULAR);
                                break;
                            case THIN:
                                builder.add(THIN);
                                break;
                        }
                    }
                }else{
                    if(robotoFirstChoices.contains(builder.get(0))){
                        String id = (Character.toUpperCase(part.charAt(0))) + part.toLowerCase().substring(1);
                        switch (id){
                            case ITALIC:
                                builder.add(ITALIC);
                                break;
                        }
                    }
                }


            }else if(type.equalsIgnoreCase(CONDENSED)){

                if(builder.size() == 0){
                    if(condensedChoices.contains(part.toLowerCase())){
                        String id = (Character.toUpperCase(part.charAt(0))) + part.toLowerCase().substring(1);
                        switch (id){
                            case BOLD:
                                builder.add(BOLD);
                                break;
                            case ITALIC:
                                builder.add(ITALIC);
                                break;
                            case LIGHT:
                                builder.add(LIGHT);
                                break;
                            case REGULAR:
                                builder.add(REGULAR);
                                break;
                        }
                    }
                }else{
                    if(condensedFirstChoices.contains(builder.get(0))){
                        String id = (Character.toUpperCase(part.charAt(0))) + part.toLowerCase().substring(1);
                        switch (id){
                            case ITALIC:
                                builder.add(ITALIC);
                                break;
                        }
                    }
                }

            }


        }

        // Construct Post String
        String result = "";
        for(String part: builder){
            result = result.concat(part);
        }

        return result;
    }


}
