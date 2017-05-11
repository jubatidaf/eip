# eip
###1. Présentation

Le projet a pour but de présenter les **EIP** afinn d'intégrer les services web.
On a développé un package **eip** qui contient 2 classes principales:

ProducerConsumer.java repond au question 1,2,3
  lancé le service MyServiceController.java
  puis lancé la classe ProducerConsummer.java
    -il vous demande de saisir
        -si vous saisissez "exit" va stopé la communication
        -si vous saisissez animal va vous demander de nommé le endpoint, puis de saisir le nom de l'animal à chercher.
        -si autre il retourne ce que vous avez sasie.
        
producerConsumer2.java repond a la question plusieur instances.
  -lancé les deux service MyServiceController.java et MyServiceController2.java.
  -saisie un nom d'animal que vous voulez checher s'il le trouve pas dans le premiers service il le cherche dans le deuxieme.
  -sinon s'il le trouve pas, il affiche un message "animal n'existe pas"
      -jeux de test:
          -un nom d'animal qui existe dans MyServiceController.java (Tic)
          -un nom d'animal qui existe dans MyServiceController2.java (Ticjub)

