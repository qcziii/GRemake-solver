# G3Solver

Solver BFS do zagadki z wytrychami (Gothic Remake).  
Program przyjmuje uproszczony model zamka i zwraca najkrotsza sekwencje ruchow.

Domyslnie program dziala interaktywnie i pyta krok po kroku o cala konfiguracje puzzla w konsoli.

## Zasady modelu

- rzędy liczymy **od dolu do gory**
- kazdy rzad ma zawsze **7 pozycji** (`0..6`)
- rzad otwiera sie w **srodkowej pozycji**, wiec stan docelowy to zawsze `index 3`
- **LEWO** zwieksza indeks pozycji (np. `3 -> 4`), a **PRAWO** go zmniejsza (np. `3 -> 2`)
- ruch jednego rzedu moze przesunac inny rzad tylko o **-1, 0 albo 1**
- oddzialywania sa **symetryczne**: jesli ruch w lewo przesuwa jakis rzad w lewo, to ruch w prawo przesunie go o tyle samo w prawo
- ruch, ktory wypchnalby dowolny rzad poza zakres `0..6`, jest **niedozwolony** (brak zawijania)
- przy wpisywaniu `deltas` zachowujesz poprzednia techniczna konwencje: opisujesz bazowy ruch `3 -> 2`, a przeciwny kierunek wylicza sie automatycznie

## Format wejscia JSON

```json
{
  "rowCount": 3,
  "startPositions": [2, 4, 1],
  "maxVisitedStates": 200000,
  "influences": [
    { "deltas": [-1, 1, 0] },
    { "deltas": [1, -1, 1] },
    { "deltas": [0, 1, -1] }
  ]
}
```

Znaczenie pol:
- `startPositions[i]` - aktualna pozycja rzedu `i`, gdzie `i = 0` oznacza dolny rzad
- `influences[r].deltas[k]` - o ile zmienia sie rzad `k` dla bazowego ruchu technicznego `3 -> 2`
- ruch w prawo automatycznie bierze wartosc przeciwna do `deltas`
- `maxVisitedStates` - bezpiecznik BFS na duze przestrzenie stanow

Uwagi:
- `rowSizes` jest opcjonalne, ale jesli je podasz, kazda wartosc musi byc rowna `7`
- `targetPositions` jest opcjonalne, ale jesli je podasz, kazda wartosc musi byc rowna `3`
- wszystkie wartosci w `deltas` musza nalezec do zbioru `-1, 0, 1`
- stary format `left/right` jest nadal akceptowany, ale tylko wtedy, gdy `right` jest dokladnym przeciwienstwem `left`

## Szybki start

```powershell
mvn test
mvn exec:java
mvn exec:java "-Dexec.args=src/main/resources/sample-puzzle.json"
```

Program wypisuje liczbe ruchow i pelna sekwencje (ktory rzad od dolu i kierunek).

- `mvn exec:java` - tryb interaktywny (kreator konfiguracji)
- `mvn exec:java "-Dexec.args=<sciezka>"` - tryb plikowy JSON

