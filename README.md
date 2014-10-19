# deadSkunk
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.r0adkll/deadskunk/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.r0adkll/deadskunk)

This is my personal Android library filled with useful tools that i have built over the year, i continue to improve them as i continue my job as an Android Developer.

## Features

* **BetterListAdapter**      - A list adapter that re-uses views and is far more efficient than the plain way
* **BetterExpandableListAdapter** - An expandable list adapter that re-uses views with the ViewHolder pattern
* **SimpleSectionAdapter**   - A simple adapter for applying non-sticky listview sections and section headers
* **BCrypt**                 - the handy hashing and crypto library 
* **FNV Hashing**            - FNV Hash utility
* **CacheTool**              - a simple helper class for writing data to the cache partition
* **FileUtils**              - a simple helper class for writing/reading data from the files partition, as well as external sdCard
* **ProgressInputStream**    - a utility for listening for transfer progress updates
* **SecurePreferences**      - a helper class for encrypting preference files
* **Utils**                  - the generic utils class, this contains a random assortment of general help functions, such as switchable logging, conversions, randoms, etc.
* **AspectRatioImageView**   - an image view that maintains the aspect ratio of the source image
* **SmoothSeekBarChangeListener** - a helper class that smooths out seekbar seeking no matter the range of values
* **IntentUtils**			  - Intent Utility for common intents [IntentUtils](https://github.com/d-tarasov/android-intents)

## Importing
Include this in your gradle file

	compile 'com.r0adkll:deadskunk:+'

## Special Note
Some of the features that were once in this library have been removed in favor of a better counter-part library that i segmented into it's own entity. Here are the list of my libraries which evolved from some of the features:

-	`r0adkll/PostOffice` - A library for creating/displaying Dialogs in Holo or Material Design.
-	`52inc/FontLoader` - A library for easily applying Roboto fonts to text fields.
-	`52inc/Attributr` - A library for displaying beautiful license attributions


## Author
* **r0adkll** (Drew Heavner) - [r0adkll.net](http://r0adkll.net)


## License

    MIT License (MIT)

    Copyright (c) 2013 Drew Heavner
  
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
  
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
  
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

