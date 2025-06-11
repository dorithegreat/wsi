
# Bot do Listy 3 WSI

Projekt napisany jest w javie z mavenem. Dołączony plik zip zawiera potrzebne pliki razem ze strukturą folderów wymaganą przez mavena. Ze względu na trudności w przekazywaniu argumentów z terminala do programów z mavenem dołączony jest również skrypt run.sh który służy do uruchamiania programu. Do skryptu należy podać takie same argumenty jakie wymienione są w opisie zadania, t.j.:
```
bash run.sh server_address port player username depth
```

Program wykorzystuje jedynie podstawowe biblioteki javy i nie wymaga żadnych dodatkowych instalacji. Projekt był tworzony i testowany w javie 23, na systemie Linux Mint 22

W pliku zip zawarte są dwie klasy

- App.java - odpowiada za komunikację z serwerem, wywoływanie algorytmu minimax, oraz zawiera funkcję main

- Minimax.java - zawiera algorytm minimax i powiązane z nim funkcje pomocnicze. Szczególnie istotną funkcją jest funkcja heurystyki o nazwie evaluateState, której działanie opisane jest w komentarzach w kodzie nad jej deklaracją
