# UDP Project
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)
![UDP](https://img.shields.io/badge/Protocol-UDP-lightgrey)
![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven)

In diesem Projekt wird eine Datei über UDP übertragen.

Die Datei wird in kleine Teile zerlegt, verschickt und beim Empfänger wieder zusammengesetzt.  
Da UDP keine Garantie für eine fehlerfreie Übertragung gibt, wird am Ende ein MD5-Hash verglichen.

---

## Start

Zuerst den Receiver starten:

```
MainRX
```

Danach den Sender:

```
MainTX 
```

---

## Ergebnis

Wenn alles funktioniert hat, erscheint:

```
SUCCESS: File received correctly
```

Zusätzlich wird auf der Empfängerseite eine neue Datei erstellt:

```
received_test.txt
```

Diese Datei enthält den empfangenen Inhalt und kann mit der Originaldatei verglichen werden.
