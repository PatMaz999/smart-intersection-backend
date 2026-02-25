# Smart Intersection
## Adaptacyjny System Zarządzania Ruchem

---

## Spis treści
1. [Uruchomienie](#uruchomienie)
2. [Opis algorytmu sterującego ruchem](#Opis-algorytmu-sterującego-ruchem)
3. [Dokumentacja API](#dokumentacja-api)
4. [Testy](#testy)
5. [Stack technologiczny](#stack-technologiczny)
6. [Architektura systemu](#architektura-systemu)
7. [Możliwości rozwoju](#możliwości-rozwoju)

---

### Uruchomienie

* **Linux / macOS / Windows (PowerShell):** ./gradlew bootRun
* **Windows (Klasyczny Wiersz Poleceń / CMD):** gradlew.bat bootRun

### Dostęp po uruchomieniu

* Na potrzeby prezentacji systemu udostępniona została wizualizacja działania, pozwalająca przetestować system (również na niestandardowych przypadkach): [Wersja demonstracyjna](http://localhost:8080)
* W standardowych warunkach komunikacja z systemem odbywa się przez REST API: [Dokumentacja API](#dokumentacja-api)

---

### Opis algorytmu sterującego ruchem

#### Główne cechy
* **Optymalizacja pustych przebiegów:** System automatycznie zmienia stan sygnalizacji, gdy wykryje brak samochodów mogących opuścić skrzyżowanie na pasach z aktualnie zielonym światłem.

* **Zarządzanie stanem spoczynku:** W przypadku całkowicie pustego skrzyżowania, stan świateł nie jest zmieniany, chyba że obecna konfiguracja jest nieoptymalna. W takiej sytuacji system samoczynnie wraca do domyślnego stanu sygnalizacji.

* **Sterowanie oparte na czasie oczekiwania:** Głównym czynnikiem sterującym ruchem jest czas oczekiwania pojazdów. Gdy samochód na czerwonym świetle oczekuje zbyt długo, algorytm wymusza zmianę świateł, gwarantując mu możliwość przejazdu.

* **Inteligentna obsługa lewoskrętów:** System aktywnie wykrywa zablokowane lewoskręty. Gdy pojazd skręcający w lewo przekroczy maksymalny czas oczekiwania lub algorytm wyliczy, że nie opuści skrzyżowania przed upływem maksymalnego czasu oczekiwania, system przydziela dla jego pasa dedykowaną, bezkolizyjną fazę ruchu, odcinając ruch kolizyjny.

* **Adaptacja do zwiększonego ruchu:** Algorytm zawsze rozpatruje czasy oczekiwania zaczynając od pasa z największym opóźnieniem. Zapobiega to błędom decyzyjnym, gdy więcej niż jeden kierunek przekracza dozwolony czas oczekiwania.

* **Dynamiczna długość fazy:** Liczba samochodów, która będzie mogła przejechać przez skrzyżowanie podczas jednego cyklu, jest obliczana proporcjonalnie do liczby oczekujących pojazdów. System jednak nigdy nie przekroczy oraz minimalnej (za wyjątkiem sytuacji, gdy pas będzie pusty).

#### Dodatkowe cechy
* **Całkowita modularność algorytmu:** Algorytm sterujący ruchem został zaimplementowany jako łańcuch zależności. W bardzo łatwy sposób można do niego dodawać nowe zasady, zmieniać ich kolejność lub usuwać istniejące.

#### Zasady symulacji

* **Czas podzielony na cykle:** Symulacja opiera się na dyskretnych krokach (cyklach). Kolejny stan skrzyżowania obliczany jest tylko po wywołaniu zdarzenia kroku (np. komendy `step`).

* **Całkowita zmiana świateł zajmuje jeden cykl.**

* **Samochody nie przejeżdżają podczas fazy żółtego światła:** Pozwala to zachować pełną deterministyczność symulacji. Można to łatwo zmienić, modyfikując metodę `canMove` w klasach `ClearancePhase`, `ClearSingleLane`.

* **Przepustowość cyklu:** Podczas trwania jednego cyklu, z każdego pasa, skrzyżowanie może opuścić maksymalnie jeden pojazd. (Dla obecnie zaimplementowanego skrzyżowania jednojezdniowego).

---

### Dokumentacja API

#### Endpoint symulacji

**Run simulation**

* **URL:** `/simulate`
* **Method:** `POST`
* **Body:**
```json
{
  "commands": [
    {
      "type": "addVehicle",
      "vehicleId": "V1",
      "startRoad": "south",
      "endRoad": "north"
    },
    {
      "type": "step"
    }
  ]
}
```

* **Success Response:**
    * **Code:** 200
    * **Content:**
```json
{
  "stepStatuses": [
    {
      "leftVehicles": [
        "V1"
      ]
    },
    {
      "leftVehicles": []
    }
  ]
}
```

* **Error Response:**
    * **Code:** 400
      * **Content:** `{ "message": "invalid argument" }`

    * **Code:** 422 
      * **Cause** Returned when data is correct, but intersection configuration does not support specific operation
      * **Content:** `{ "message": "Unsupported turn direction SOUTH to SOUTH" }`

---

### Testy

* **Kluczowa logika:** Kluczowa logika aplikacji odpowiadająca za ustalanie pierwszeństwa przejazdu oraz sterowanie ruchem zawiera pokrycie testami wynoszące `ponad 90%` linii kodu.
* **Zapobieganie kolizjom:** Testy pokrywają `wszystkie` możliwe przypadki dotyczące przejazdów na skrzyżowaniu, wykluczając możliwość wystąpienia kolizji.

---

### Stack technologiczny

* **Język programowania:** `Java 25`
* **Architektura:** `Architektura Heksagonalna`
* **Metodyka:** `Domain Driven Design`
* **Framework:** `Spring Boot 4` (izolowany od warstwy domeny)
* **Narzędzie budowania:** `Gradle`
* **Testowanie:** `JUnit 5`, `Mockito`

---

### Architektura systemu

Priorytetem podczas tworzenia systemu było zapewnienie **maksymalnej rozszerzalności** w celu umożliwienia bardzo łatwego implementowania nowych funkcjonalności.

* **Model domenowy:** Reprezentuje skrzyżowanie oraz możliwe do wykonania akcje związane ze skrzyżowaniem.
* **Warstwa aplikacji:** Odpowiada za przeprowadzenie symulacji zgodnie z założonymi wymaganiami. Możliwe jest zaimplementowanie symulacji składającej się z kilku skrzyżowań lub nawet utworzenie całego miasta.

#### Najważniejsze komponenty

* `Intersection:` Interfejs umożliwiający tworzenie różnych rodzajów skrzyżowań. Dostępne implementacje: `StandardIntersection`.
    * `LanesGroup`: Interfejs reprezentujący pasy dojazdowe należące do skrzyżowania. Dostępne implementacje: `StandardLanes`.
    * `LightState`: Interfejs reprezentujący stan świateł. Określa on również, czy samochód może opuścić skrzyżowanie za pomocą metody `canMove`. Dostępne implementacje: `StraightLineGreen`, `SingleLaneGreen`, `ClearancePhase`, `ClearSingleLane`.
    * `TrafficStrategy`: Interfejs reprezentujący strategię zmiany świateł. Podejmuje on decyzje dotyczące zmiany stanu świateł oraz wybiera nowy stan `canMove`. Jest on implementacją wzorca projektowego **Łańcuch zależności** i wykorzystuje handlery rozszerzające abstrakcyjną klasę `AbstractTrafficHandler`. Dostępne implementacje: `StandardStrategy`.

* `AbstractTrafficHandler`: Metoda abstrakcyjna służąca do tworzenia handlerów wykorzystywanych do podejmowania decyzji dotyczącej zmiany świateł. Dostępne implementacje: `EmptyGreenHandler`, `EmptyRoadHandler`, `PriorityHandler`.

---

### Możliwości rozwoju

* **Różne priorytety pojazdów:** System umożliwia dodanie pojazdów z wyższym lub niższym priorytetem przejazdu. Nowy typ musi implementować interfejs `Vehicle`. Nowy typ może posiadać inny priorytet zwracany metodą `getPriority()` oraz posiadać inną logikę zwiększania priorytetu podczas oczekiwania `incrementPriority()`.

* **Różne konfiguracje pasów dojazdowych:** Nowa konfiguracja powinna implementować interfejs `LanesGroup`. Wystarczy zmienić sposób tworzenia nowych klas implementujących interfejs `Lane`. Można w ten sposób utworzyć konfiguracje skrzyżowania z trzema drogami dojazdowymi.

* **Różne priorytety pasów dojazdowych:** Nowa konfiguracja powinna implementować interfejs `Lane`. W nowej implementacji wystarczy zmienić metodę `getPriority()`. Można to zrobić na przykład uwzględniając przy zwracanej wartości wagę.

* **Nowe konfiguracje stanów świateł:** Nowy stan powinien implementować interfejs `LightState` (polecam wykorzystanie abstrakcyjnej klasy `AbstractLightState`). Klasa abstrakcyjna musi posiadać metodę `canMove` określającą pierwszeństwo przejazdu nowego stanu. Nowe stany będą przydatne przy implementacji skrzyżowań z wielojezdniowymi dojazdami.

* **Nowe strategie zmiany świateł:** Można utworzyć całkowicie nowe zasady zmiany świateł implementując `TrafficStrategy` (na przykład standardowe nieinteligentne) lub dostosować istniejącą `StandardStrategy`, która jest łańcuchem zależności zawierającym klasy rozszerzające klasę `AbstractTrafficHandler`. Handlery jeden po drugim podejmują decyzję o zmianie stanu świateł lub delegują podjęcie decyzji do kolejnego. Jeżeli żaden nie podejmie decyzji, to przedłużany jest obecny stan świateł. Można dowolnie dodawać nowe, usuwać lub zmienić kolejność handlerów.

* **Wielopasowe drogi dojazdowe:** Możliwe jest utworzenie nowej implementacji `LanesGroup` dla dojazdów wielojezdniowych. Standardowe stany świateł oraz standardowe strategie powinny być kompatybilne z wielopasowymi skrzyżowaniami, jednak nie będą efektywne. W związku z tym polecam utworzenie dla nich nowych stanów świateł drogowych oraz utworzenie nowej strategii opartej o istniejące handlery.