# G3Solver

Solver BFS do zagadki z wytrychami (Gothic Remake).  
Podajesz konfiguracje zamka w JSON (stan startowy, docelowy, liczba rzedow i wplyw ruchu kazdego rzedu na pozostale), a program zwraca najkrotsza sekwencje ruchow.

Domyslnie program dziala interaktywnie i pyta krok po kroku o cala konfiguracje puzzla w konsoli.

## Format wejscia JSON

```json
{
  "rowCount": 3,
  "rowSizes": [5, 5, 5],
  "startPositions": [2, 4, 1],
  "targetPositions": [0, 0, 0],
  "maxVisitedStates": 200000,
  "influences": [
    { "left": [-1, 1, 0], "right": [1, -1, 0] },
    { "left": [1, -1, 1], "right": [-1, 1, -1] },
    { "left": [0, 1, -1], "right": [0, -1, 1] }
  ]
}
```

Znaczenie pol:
- `rowSizes[i]` - liczba mozliwych pozycji rzedu `i` (modulo, np. 5)
- `startPositions[i]` - aktualna pozycja rzedu `i`
- `targetPositions[i]` - pozycja docelowa rzedu `i` (domyslnie same zera)
- `influences[r].left[k]` - o ile zmienia sie rzad `k`, gdy przesuniesz rzad `r` w lewo
- `influences[r].right[k]` - analogicznie dla ruchu w prawo
- `maxVisitedStates` - bezpiecznik BFS na duze przestrzenie stanow

## Szybki start

```powershell
mvn test
mvn exec:java
mvn exec:java "-Dexec.args=src/main/resources/sample-puzzle.json"
```

Program wypisuje liczbe ruchow i pelna sekwencje (ktory rzad i kierunek).

- `mvn exec:java` - tryb interaktywny (kreator konfiguracji)
- `mvn exec:java "-Dexec.args=<sciezka>"` - tryb plikowy JSON

