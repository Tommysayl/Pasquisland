# SGS-Project

## isola di pasqua
Vogliamo creare un'isola modellandola con una matrice nxn. In quest'isola ci sono delle tribù, che si evolvono e interagiscono tra loro.   
Abbiamo stabilito le caratteristiche che definiscono gli omini appartenenti alle diverse tribù: 
### caratteristiche fisse
Forza, Socievolezza.  
### caratteristiche variabili
Fame. 
La fame sale ogni turno di una quantità x e, se superata una certa soglia, porta l'unità a morire. 

## Interazioni
**Tutte le interazioni avvengono in maniera probabilistica, e non stabilita a priori dai parametri degli omini**
Le interazioni sono di 3 tipi: Tra omini della stessa tribù, tra omini di tribù diverse, tra omino e pianta. 
In quest'ultimo scenario, vogliamo che L'omino mangi la pianta e si muova nella sua casella. Nel caso di interazione tra omini della stessa tribù, vogliamo che i due omini si riproducano, a meno che il valore della "fame" non superi socialità * k. Nel caso rimanente, calcolo Forza + Fame - socievolezza. "Lancio una moneta" e in base al risultato l'omino che ha priorità di turno decide se mandare un messaggio di pace o di morte. L'altro omino risponde in base allo stesso parametro. 
**la griglia non viene aggiornata tutta insieme, ma a partire da sinistra verso destra, poi dall'alto verso il basso.**

## spawn delle tribù
Divido la isola in tot sottoaree, e faccio spawnare tot. omini a random. Ogni omino appartiene alla tribù della sottoarea in cui è nato, e i suoi valori di Forza e Socievolezza iniziali partono come numeri random compresi tra i valori x e y (forza e soci.) che delimitano la tribù nel piano. 

## spawn delle piante
game of plants da decidere/random







