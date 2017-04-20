# 8051 MCU Emulator
Być może, jak starczy mi sił, zapału i chęci powstanie tutaj kiedyś emulator, a tak dokładniej to całe IDE, układu 8051.
### Co już działa
Komendy:  
1.ORG  
2.MOV  
3.DJNZ  
4.LJMP  
5.RR  
6.RC  
7.RLC  
8.RRC  
Porty P0-P3, Rejestry R0-R7, Wyswietlanie stanu rejestru psw, w którym działają bity: C,AC,P
### Przykładowy działający program  
Program rotuje odbijający się od krawędzi stan wysoki po porcie zerowym  
ORG 0000h  
mov a,#01h  
mov r1,#08h  
mov p0,a  
wybor:  
djnz r1,lewo  
mov r1,#08h  
wybor2:  
djnz r1,prawo  
mov r1,#08h  
ljmp wybor  
lewo:  
rl a  
mov p0,a  
ljmp wybor  
prawo:  
rr a  
mov p0,a  
ljmp wybor2  
### Cele na koniec
Celem końcowym, jest implementacja możliwości budowania własnych układów w edytorze graficznym, podłączając komponenty do mikroprocesora
analogicznie do korzystania z płytki stykowej.
