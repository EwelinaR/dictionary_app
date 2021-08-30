## Dictionary app
Desktop application for translating English words to Polish language.
It translates a word that is currently in clipboard using the [diki](https://www.diki.pl) dictionary.

To use this dictionary you have to copy (`CTRL + C`) selected word and use shortcut `CTRL + space`. 
Then, the window at the top of the screen will pop up. 
It will contain:
- english base form of the word
- polish definition
- audio with pronunciation
- phonetic transcription
- images
- examples of usage

### Technologies / libraries
- javaFX - semitransparent window with word definition
- jnativehook library - global keyboard listener
