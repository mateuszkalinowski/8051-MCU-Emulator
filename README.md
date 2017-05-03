# 8051 MCU Emulator
Docelowo - emulator mikroprocesora 8051 wraz z jego otoczeniem (wyświetlacze, przyciski, zadajniki, diody).
### Co już działa
Komendy:  
1.ORG  
2.ADD  
3.ADDC  
4.ANL  
5.CLR  
6.CPL  
7.DEC  
8.DJNZ  
9.INC  
10.JB  
11.JBC  
12.JNB  
13.JNC  
14.LJMP  
15.MOV  
16.NOP  
17.ORL  
18.POP  
19.PUSH  
20.RETI  
21.RL  
22.RLC  
23.RR  
24.RRC  
25.SETB  
26.SUBB  
27.SWAP  
28.XRL  
+  
Cała pamięć Ram niższego poziomu i część sfr, adresowanie bezpośrednie i przez rejestry R0 i R1, timery 0 i 1 pracujące w trybach 0, 1 i 2. 7 przycisków, 7 zadajników podłączonych równolegle do portu 2, podwójny wyświetlacz 7-seg z konwerterami bcd-7seg podłączony do portu 1, linijka świetlna z diod podłączona do portu 0. Porty wyświetlacza i linijki świetlnej można edytować w menu konfiguracji programu.
### Przykładowe działające programy  
Program prezentujący działanie timer'a:
org 0000h  
ljmp 00A0h  
org 00A0h  
	mov tmod,#02h  
	mov th0,#80h  
	mov tl0,#00h  
	setb ea  
	setb et0  
	setb tr0  
	mov a,#01h  
	mov p0,a  
start: ljmp start  
org 000Bh  
	rl a  
	mov p0,a  
reti  
  
Licznik odliczający od 0-99, działający przy włączonym zadajniku 7, resetowany przyciskiem 6:
org 0000h  
start:  
	mov a, #00h  
	mov r2, #00h  
	mov r3, #00h  
	mov r4, #0Ah  
	mov r5, #0Ah  
	mov r6, #00h  
	mov p1, a  
petla:  
	jb p2.6, pomin  
	mov a, #00h  
	mov r2, #00h  
	mov r3, #00h  
	mov r4, #0Ah  
	mov r5, #0Ah  
	mov r6, #00h  
	mov p1,a  
pomin:  
	jb p2.7,petla  
	mov a,r3  
	swap a  
	orl a,r2  
	mov p1,a  
	mov a,r2  
	add a,#01h  
	mov r2,a   
	djnz r4,ccc  
	mov r4,#0Ah  
	mov r2,#00h  
	mov a,r3  
	add a,#01h  
	mov r3,a   
	djnz r5,ccc  
	mov r5,#0Ah  
	mov r3,#00h  
ccc:  
	ljmp petla  

 
### Cele na koniec
Celem końcowym, jest implementacja możliwości budowania własnych układów w edytorze graficznym, podłączając komponenty do mikroprocesora
analogicznie do korzystania z płytki stykowej.
