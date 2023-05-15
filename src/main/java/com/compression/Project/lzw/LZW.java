package com.compression.Project.lzw;

import java.util.*;
import java.util.stream.Collectors;

public class LZW {
    public static List<Object> encode(String inputString) {
        //przygotowanie stringa po przesłaniu ze strony
        inputString = inputString.substring(0,inputString.length()-1);

        // utworzenie mapy przechowującej kody dla poszczególnych ciągów znaków
        Map<String, Integer> table = new HashMap<>();
        for (int i = 0; i <= 255; i++) {
            String ch = "";
            ch += (char)i;
            table.put(ch, i);
        }

        // inicjalizacja zmiennej przechowującej poprzedni ciąg znaków oraz listy kodów wyjściowych
        String p = "", c = "";
        p += inputString.charAt(0);
        int code = 256;
        List<Integer> outputCode = new ArrayList<>();

        // iteracja po ciągu wejściowym
        for (int i = 0; i < inputString.length(); i++) {
            // jeśli nie jest to ostatni znak, to dodajemy kolejny znak do ciągu c
            if (i != inputString.length() - 1) {
                c += inputString.charAt(i + 1);
            }

            // jeśli dany ciąg znaków znajduje się już w mapie, to aktualizujemy poprzedni ciąg znaków p
            if (table.containsKey(p + c)) {
                p = p + c;
            }
            // w przeciwnym razie dodajemy kod poprzedniego ciągu do listy wyjściowej
            // oraz dodajemy nowy ciąg znaków do słownika z kolejnym wolnym kodem
            else {
                outputCode.add(table.get(p));
                table.put(p + c, code);
                code++;
                p = c;
            }
            // zerujemy ciąg c
            c = "";
        }
        // dodajemy kod ostatniego ciągu do listy wyjściowej i zwracamy ją
        outputCode.add(table.get(p));

        Map<String, Integer> returnTable = table.entrySet().stream()
                .filter(entry -> entry.getValue() > 255)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Arrays.asList(outputCode, returnTable);
    }

    // Metoda dekodująca
    public static String decode(String inputString) {

        inputString = inputString.substring(0,inputString.length()-1).replace("%2C",",");
        List<Integer> op = Arrays.stream(inputString.split(","))
                .map(Integer::parseInt)
                .toList();

        // Tworzenie tabeli z kodami ASCII
        Map<Integer, String> table = new HashMap<>();
        for (int i = 0; i <= 255; i++) {
            String ch = "" + (char)i;
            table.put(i, ch);
        }

        StringBuilder result = new StringBuilder();
        int old = op.get(0), n;
        String s = table.get(old);
        String c = "";
        c += s.charAt(0);
        result.append(s);
        int count = 256;

        // Przetwarzanie ciągu wyjściowego
        for (int i = 0; i < op.size() - 1; i++) {
            n = op.get(i + 1);
            if (!table.containsKey(n)) {
                s = table.get(old);
                s = s + c;
            } else {
                s = table.get(n);
            }
            result.append(s);
            c = "";
            c += s.charAt(0);
            table.put(count, table.get(old) + c);
            count++;
            old = n;
        }

        return result.toString();
    }
}
