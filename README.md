# Square-Editor

This is a coursework project of COMP208@LIV. 
Square Editor is a rich Text Editor written in JavaFX. It was intended to provide a different editing experience with preset formatting of different sections. Nevertheless, this is a very naive project and doesn't work well in practice.

## Why it's a naive work?

Unlike what they have done in Swing, JavaFX doesn't provide rich text facilities for a TextArea. Hence, the Square Editor relies too much on a very naive-implemented rich text system. In short, the underlying rich text system will require too much system resources in order to draw texts and other components when loading a thousand-line file. Besides, we don't have access to some API of font metrics. Treat this as a practice project on using JavaFX will be fine.
