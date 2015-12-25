# vmgparser-lib

vmgparser-lib is a tiny library that is able to parse VMG files.

#### Description
Vmg, commonly called vMessage, is used for the storage and exchange of short messages, typically SMS messages, between mobile devices and desktop computers (Wikipedia). This file format has been standardized by the Infrared Data Association (IrDA) with the Ir Mobile Communications (IrMC v.1.1) specification published in 1999.
Vmg format is close to the vCard syntax. Below is a typical example of a .vmg file:

```
BEGIN:VMSG
VERSION:1.1
BEGIN:VCARD
VERSION:2.1
N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:;Coralie
FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:Coralie
TEL;CELL; HOME; VOICE:89762772080
END:VCARD

BEGIN:VENV
BEGIN:VBODY
X-IRMC-BOX:INBOX
X-IRMC-STATUS:READ
Date:2013.4.3.23.5.9
TEXT;ENCODING=QUOTED-PRINTABLE:Good night. :). Sweet dreams. 
END:VBODY
END:VENV
END:VMSG
```

#### Features
* Parse .vmg files in Java Object.
* Most advanced .vmg parser on the web.
* Under active development.

#### Build instructions

To parse vCard that is embeded in some .vmg files, vmgparser-lib requires the [ez-vcard library](https://github.com/mangstadt/ez-vcard). You have 2 solutions to embed vmgparser-lib in your project:

1. Include the pre-compiled .jar binary in your project.
2. Include sources directly in your project.

#### Credits

This project is the fruition of **Louis-Paul CORDIER**. Please consider donation if you like this project!

#### News

**December 25, 2015**

Merry Christmas everybody! As the project is reaching a good maturity, I decided to start publishing the source-code online ;)
