## Sources

### Consommation d'électricité des raffineries

En commentaires, j’ai eu plusieurs fois l’idée que le raffinage des carburants demandait beaucoup d’électricité. C’est FAUX. [Les raffineries consomment 3,7 TWh](http://encyclopedie-dd.org/encyclopedie/neige-neige-sciences-et-techniques/a-3-faits-et-chiffres/les-consommations-d-energie-en.html) (et pas juste pour les carburants automobiles !). Un petit article sur cette question: [Vérification faite : Combien d’électricité pour un litre d’essence?](https://www.lesoleil.com/actualite/verification-faite/verification-faite--combien-delectricite-pour-un-litre-dessence-2b4240fccdd01278f25d5a6408bd2599)

### Outil pour comparer voitures électriques et thermiques

[Climobil](https://climobil.connecting-project.lu/?batteryLifetime=240000&batteryCapacity=30&greenhouseGas=65&electricCarRange=250&carbonElectricityMix=300&greenhouseBattery=30&greenhouseWTT=25&greenhouseTTW=150&batteryPenalty=0.9&annualMileage=20000&ICECurb=1551&ECurb=1977&NEDCpenalty=0.39&decarbonization=0) est un super site qui vous permettra de comparer des modèles de voiture électrique et de voiture thermique en ajustant vous même les paramètres clefs (n’hésitez pas à utiliser les paramètres avancés). **Utilisez le !**

### Analyses de la voiture électrique

- La société de conseil, [Ricardo](https://ricardo.com/about-us/what-we-do), a fait [une très grosse étude sur les impacts environnementaux de la voiture électrique en Europe](https://climate.ec.europa.eu/system/files/2020-09/2020_study_main_report_en.pdf). (J’ai récupéré en bas de [cette page](https://www.greencarcongress.com/2020/09/20200902-ricardo.html) les données de l’étude.)
- [L’avis de l’ADEME de 2016 sur la voiture électrique](https://librairie.ademe.fr/mobilite-et-transport/1922-the-potential-of-electric-vehicles.html) ([une étude plus récente et plus vaste](https://www.fondation-nicolas-hulot.org/quelle-contribution-du-vehicule-electrique-a-la-transition-energetique/) est disponible sur le site de la FNH, lien en bas de page). L’avis de 2016 se base, en partie, sur une [étude plus ancienne de l’ADEME](https://librairie.ademe.fr/cadic/3285/_90511__acv-comparative-ve-vt-resume.pdf).  
- [Carbone 4 a aussi fait des études sur la voiture électrique](http://www.carbone4.com/voiture-electrique-etre-decarbonee-de-production-a-lusage/).
- Knobloch, Florian, et al. [Net emission reductions from electric cars and heat pumps in 59 world regions over time.](https://www.nature.com/articles/s41893-020-0488-7?_cldee=cHdjLW1lZGlhQGZyZWVsaXN0cy5vcmc%3D&recipientid=account-6c987eae0e73e9119133005056ac4b59-20ca80049e7c46709475151bfae653b8&esid=916b395a-e36c-ea11-914a-005056ac4b59) _Nature sustainability_ 3.6 (2020): 437-447.

### Données pour les calculs des émissions de CO2

- [Spritmonitor](https://www.spritmonitor.de/) permet d’accéder aux consommations de voitures renseignées par des utilisateurs ([une actualité de PSA qui montre que Spritmonitor est proche des données réelles](https://www.groupe-psa.com/fr/actualites/corporate/groupe-psa-fne-te-bureau-veritas-publient-rapport-detaille-mesures-de-consommation-usage-reel/), voir la [figure ici](https://theicct.org/news/real-world-vehicle-fuel-consumption-gap-europe-stabilizing)).
- L’ADEME propose une quantification [des émissions de CO2 des différents carburants](https://www.bilans-ges.ademe.fr/documentation/UPLOAD_DOC_FR/index.htm?new_liquides.htm) mais les données pour les émissions du diesel (B7) ne sont pas bonnes (et pas cohérentes avec le reste de la page). Je me suis donc basé sur ce [guide méthodologique du Ministère de la Transition écologique et solidaire pour les émissions d’équivalent CO2 par litre de carburant](https://www.ecologie.gouv.fr/sites/default/files/Info%20GES_Guide%20m%C3%A9thodo.pdf).
- J’ai pris un rendement de 85% pour la charge en en discutant avec certains de mes relecteurs qui connaissent bien le sujet. On trouve une quantification similaire ici: [Un an de mesure de charge et d’autonomie par un utilisateur de Zoé](https://www.amperes.be/2019/03/26/un-an-de-mesures-de-charge-et-dautonomie/) et là [Quel est le rendement réel des voitures électriques](http://www.fiches-auto.fr/articles-auto/electrique/s-2399-rendement-reel-des-voitures-electriques.php) (un peu plus de perte sur le réseau et un peu moins à la charge… presque la même chose au final).
- Les pertes sur le réseau électrique sont de [2,31% sur le réseau de transport](https://bilan-electrique-2020.rte-france.com/reseau-de-transport-rte-et-son-empreinte-environnementale/) et de [6,11 % sur le réseau de distribution](https://www.enedis.fr/sites/default/files/Bilan_Electrique_Enedis_Analyse_Annuelle_2019.pdf) ([un article de connaissances des énergies sur le sujet](https://www.connaissancedesenergies.org/electricite-a-combien-s-elevent-les-pertes-en-ligne-en-france-140520)).
- J’ai utilisé les facteurs d’émission de l’ADEME pour la production électrique: [ici pour les moyens renouvelables](https://www.bilans-ges.ademe.fr/documentation/UPLOAD_DOC_FR/index.htm?renouvelable.htm) & [là pour le nucléaire et les fossiles](https://www.bilans-ges.ademe.fr/documentation/UPLOAD_DOC_FR/index.htm?conventionnel.htm).
- Pour estimer l’intensité carbone de différents pays, je me suis basé sur [ElectricityBot pour l’année 2019](https://twitter.com/BotElectricity/status/1219512372920012800/) (qui utilisent des facteurs d’émission disponibles [ici](https://gitlab.com/ThomasAuriel/BotElectricity/-/blob/master/scripts/data/co2Intensity.py)).
- [L’analyse du cycle de vie de Renault qui compare Zoé et Renault V](https://www.autoactu.com/documents/telecharger/analyse-du-cycle-vie-comparative-renault-zoe-et-clio-v) (Analyse du cycle de vie comparative: nouvelle Zoé & Clio V - Groupe Renault - Janvier 2021).
- [La Belgique sera le seul pays qui reposera plus sur les énergies fossiles en 2030 qu’aujourd’hui](https://twitter.com/EmberClimate/status/1326545256272162828). - Ember
- Une source sur la durée de vie des voitures Weymar, Elisabeth, and Matthias Finkbeiner. « [Statistical analysis of empirical lifetime mileage data for automotive LCA](https://link.springer.com/article/10.1007%2Fs11367-015-1020-6). » _The International Journal of Life Cycle Assessment_ 21.2 (2016): 215-223.

### Passages de Guillaume Pitron

- [« Une voiture électrique pollue autant qu’un diesel »](https://www.youtube.com/watch?v=bY9zESWWbjI) - Guillaume Pitron - Chaine YouTube de L’Obs
- [Guillaume Pitron dans la Terre au Carré](https://www.franceinter.fr/emissions/la-terre-au-carre/la-terre-au-carre-07-decembre-2020) (France Inter), le 7 décembre 2020.

### Pollution de l'air

- [Pollution de l’air: 48 000 morts par an](https://www.lemonde.fr/les-decodeurs/article/2019/02/27/avec-48-000-morts-par-an-en-france-la-pollution-de-l-air-tue-plus-que-l-alcool_5429074_4355770.html). - Le Monde. [L’étude de Santé Publique France a été actualisé récemment](https://www.santepubliquefrance.fr/determinants-de-sante/pollution-et-sante/air/documents/enquetes-etudes/impact-de-pollution-de-l-air-ambiant-sur-la-mortalite-en-france-metropolitaine.-reduction-en-lien-avec-le-confinement-du-printemps-2020-et-nouvelle) (Publié le 14 Avril 2021)
- [Rejets de polluants](https://ree.developpement-durable.gouv.fr/themes/pressions-exercees-par-les-modes-de-production-et-de-consommation/rejets-de-polluants/) (pour quantifier ceux qui viennent des transports).
- [Non-exhaust Particulate Emissions from Road Transport](https://www.actu-environnement.com/media/pdf/news-36643-rapport-ocde-emissions-hors-echappement.pdf).  
- [Electric vehicles are far better than combustion engine cars when it comes to air pollution. Here’s why](https://www.transportenvironment.org/newsroom/blog/electric-vehicles-are-far-better-combustion-engine-cars-when-it-comes-air-pollution).
- Beddows, David CS, and Roy M. Harrison. [PM10 and PM2. 5 emission factors for non-exhaust particles from road vehicles: Dependence upon vehicle mass and implications for battery electric vehicles.](https://www.sciencedirect.com/science/article/pii/S1352231020306208) _Atmospheric Environment_ 244 (2021): 117886.

### Statistiques mobilité en France

- [Circulation routière en France](https://www.statistiques.developpement-durable.gouv.fr/stabilite-de-la-circulation-routiere-en-france-en-2019).
- [Consommation de carburant moyenne des voitures en France](https://fr.statista.com/statistiques/486554/consommation-de-carburant-moyenne-voiture-france/).
- [Consommation carburant moyenne des véhicules utilitaires légers en France](https://fr.statista.com/statistiques/487198/consommation-carburant-moyenne-vehicules-utilitaires-legers-france/).
- [38,2 millions de voitures en circulation en France](https://www.statistiques.developpement-durable.gouv.fr/382-millions-de-voitures-en-circulation-en-france).

### Dégradation de la batterie

- [Battery Aging in an Electric Vehicle](https://batteryuniversity.com/learn/article/bu_1003a_battery_aging_in_an_electric_vehicle_ev)  
- [What can 6,000 electric vehicles tell us about EV battery health?](https://www.geotab.com/blog/ev-battery-health/) - Une analyse intéressante qui montre certains facteurs qui dégradent la santé de la batterie.

### Réseau électrique

- [Bilan électrique 2019](https://bilan-electrique-2019.rte-france.com/) - RTE  
- [Enjeux du développement de l’électromobilité pour le système électrique](https://assets.rte-france.com/prod/public/2020-05/RTE%20-%20Mobilite%20electrique%20-%20principaux%20resultats.pdf). - RTE (2019)
- [Prospective du réseau public de distribution d’électricité](https://www.enedis.fr/prospective-du-reseau-public-de-distribution-delectricite). - Enedis
- [Dépendance entre température et consommation](https://www.geotab.com/blog/ev-range/).
- D’après cette [page web](https://www.totalenergies.fr/particuliers/parlons-energie/dossiers-energie/facture-d-energie/quelle-est-la-facture-d-electricite-moyenne-pour-une-maison-ou-un-appartement), les besoins d’électricité pour l’éclairage et l’électroménager sont de 3 kWh par jour et par personne.
- Pour avoir une idée du stockage, vous pouvez regarder [la quantité d’énergie stockée par les barrages hydroélectriques](https://www.services-rte.com/fr/visualisez-les-donnees-publiees-par-rte/stock-hydraulique.html) (tous et non uniquement les STEPs).
- Pour les barrages réversibles (STEPs - Stations de transfert d’énergie par pompage) qui sont LE moyen de stockage des surplus d’électricité aujourd’hui, [cette page de l’Encyclopédie de l’Énergie](https://www.encyclopedie-energie.org/stockage-hydraulique-capacites-de-pompage-turbinage/) devrait vous donner les ordres de grandeur.
- [Dreev](https://www.dreev.com/#products-services), une filière d’EDF, met déjà en place (à petite échelle) l’injection d’électricité de la voiture électrique vers le réseau.

### Retrofit

- [L’avis de l’ADEME sur le retrofit (2021)](https://librairie.ademe.fr/cadic/5548/retrofit-2021-synthese.pdf).
- Plusieurs entreprises proposent du retrofit: [Phoenix mobility](https://www.phoenixmobility.co/), [Ian Motion](https://www.ian-motion.com/), [Transition one](https://transition-one.fr/) et [Retrofuture-EV](https://retrofuture-ev.com/produit/medium/mercedes-sl/) (spécialisation dans les vieilles voitures)
- [Rétrofit : pourquoi et comment changer sa voiture thermique en électrique ?](https://www.automobile-magazine.fr/voitures-electriques/article/27373-retrofit-pourquoi-et-comment-changer-sa-voiture-thermique-en-electrique) - Aumobile-magazine (l’autonomie paraît sous-estimé au vu des sites des constructeurs).
- [Rétrofit : légalisation de la conversion des véhicules thermiques à l’électrique](https://www.automobile-propre.com/legalisation-du-retrofit-electrique-en-france/) - Aumobile-magazine
- Deux pages du gouvernement sur lesujet: [Mettez un moteur électrique dans votre voiture : c’est le rétrofit !](https://www.gouvernement.fr/mettez-un-moteur-electrique-dans-votre-voiture-c-est-le-retrofit) et [« Le rétrofit répond aux enjeux de la mobilité et de la transition énergétique »](https://www.gouvernement.fr/le-retrofit-repond-aux-enjeux-de-la-mobilite-et-de-la-transition-energetique).

### Hybride rechargeable

Deux documents critiques basés sur des consommations réelles :  

- [Real-world usage of plug-in hybrid electric vehicles: Fuel consumption, electric driving, and CO2 emissions](https://theicct.org/publications/phev-real-world-usage-sept2020)  
- [Plug-in hybrids: Is Europe heading for a new dieselgate ?](https://www.transportenvironment.org/sites/te/files/publications/2020_11_Plug-in_hybrids_report_final.pdf)

### Couts

- [EVs will be cheaper than petrol cars in all segments by 2027, BNEF analysis finds](https://www.transportenvironment.org/press/evs-will-be-cheaper-petrol-cars-all-segments-2027-bnef-analysis-finds)  
- Une analyse qui va un peu plus loin que la mienne: [Nouvelle Renault ZOE vs Clio V : laquelle est la moins chère ?](https://www.automobile-propre.com/nouvelle-renault-zoe-vs-clio-v-laquelle-est-la-moins-chere/) - Automobile propre. Et un exercice similaire en anglais: [EV vs. Gas: Which Cars Are Cheaper to Own?](https://www.caranddriver.com/shopping-advice/a32494027/ev-vs-gas-cheaper-to-own/)
- Une étude plutôt optimiste sur le coût de la voiture électrique aux US: [Electric vehicle ownership costs: Today’s electric vehicles offer big savings for consumers](https://advocacy.consumerreports.org/wp-content/uploads/2020/10/EV-Ownership-Cost-Final-Report-1.pdf).
- Un site de promotion de la voiture électrique permet de calculer [le coût de la recharge](https://www.je-roule-en-electrique.fr/combien-coute-la-recharge-dun-vehicule-electrique-au-quotidien-704) (avec des hypothèses différentes des miennes). Un article sur [le coût de la recharge](https://www.automobile-propre.com/dossiers/cout-prix-recharge-voiture-electrique/) - Automobile propre.
- Pour aller plus loin: Palmer, Kate, et al. « [Total cost of ownership and market share for hybrid and electric vehicles in the UK, US and Japan.](https://www.sciencedirect.com/science/article/abs/pii/S030626191731526X) » _Applied energy_ 209 (2018): 108-119.
- Données utilisées: [Prix du kWh](https://www.fournisseur-energie.com/prix-kwh/), [Coût Clio V diesel (17 000 €)](https://www.elite-auto.fr/voiture-neuve/renault-1/clio-v,prix-diesel-7208.html), [Coût Clio V essence (15 000 €)](https://www.elite-auto.fr/recherche?pagination=15&paginationFrom=0%C2%A4tPage=1&availability=&utilitaire=&carsType=&marks=1&models=2576&energies=essence&finishs=&disponibilities=&doors=&transmissions=&seats=&colors=&powers=&discounts=&co2s=&motorizations=&sortBy=priceForFront,asc), [Coût Renault Zoé (35 500 € avant remise écologique)](https://www.elite-auto.fr/recherche?pagination=15&paginationFrom=0%C2%A4tPage=1&availability=&utilitaire=&carsType=&marks=&models=&energies=&finishs=&disponibilities=&doors=&transmissions=&seats=&colors=&powers=&discounts=&co2s=&motorizations=&textSearch=renault%20zo%C3%A9&sortBy=priceForFront,asc).

### Recharge des voitures électriques

- [Utilisation et recharge: Enquête comportementale auprès des possesseurs de véhicules électriques](https://www.enedis.fr/sites/default/files/field/documents/enquete-comportementale-possesseurs-de-vehicules-electriques.pdf) - Enedis.
- [Simulateur de temps de recharge](https://www.automobile-propre.com/simulateur-temps-de-recharge-voiture-electrique/). Un article sur [le temps de charge d’une voiture électrique](https://www.automobile-propre.com/dossiers/temps-de-charge-dune-voiture-electrique/).
- [Le droit à la prise](https://www.zeplug.com/blog/droit-a-la-prise-recharge-voiture-electrique-copropriete/) (vous pouvez installer une prise pour une place de parking privative dans une propriété). Et un article concernant [l’équipement des bâtiments neufs](http://www.avere-france.org/Site/Article/?article_id=7945).  
- [La prise renforcée Green up](https://www.emissionzero.fr/prise-renforcee-greenup.html) (également discutée sur [automobile-propre](https://www.automobile-propre.com/dossiers/prise-greenup-voiture-electrique/)).  
- [Les différents types de prises](https://www.automobile-propre.com/dossiers/voiture-electrique-les-differents-types-de-prises/).
- [5 choses à savoir pour bien choisir sa wallbox](https://www.automobile-propre.com/voiture-electrique-5-choses-a-savoir-pour-bien-choisir-sa-wallbox/).
- [Trouver les bornes de recharge publiques pour les voitures électriques](https://www.automobile-propre.com/dossiers/trouver-les-bornes-de-recharge-publiques-pour-les-voitures-electriques/).
- [Le gouvernement vise 100 000 bornes de recharge fin 2021](https://www.ecologie.gouv.fr/objectif-100-000-bornes-tous-mobilises-accelerer-virage-du-vehicule-electrique).
- [\[Analyses\] Infrastructures de recharge pour véhicule électrique](https://www.ecologie.gouv.fr/sites/default/files/2019-07-Rapport-IRVE.pdf). - Ministère de la transition écologique et solidaire.

### Autonomie

- __A Better Routeplanner__ vous permettra de [planifier votre trajet en véhicule électrique en fonction des points de recharge disponibles](https://abetterrouteplanner.com/?plan_uuid=085da9ad-1fc9-43bd-899f-c210646eef3a).
- [un passage de site](https://theicct.org/cards/stack/explaining-electric-vehicles) montre que l’autonomie limitée des voitures électriques ne concernera qu’une faible proportion des trajets.
- [Enjeux du développement de l’électromobilité pour le système électrique](https://assets.rte-france.com/prod/public/2020-05/RTE%20-%20Mobilite%20electrique%20-%20principaux%20resultats.pdf). - RTE (2019)

### Nécessité de réduire et faire évoluer les transports au-délà d'un simple remplacement par l'électrique

- [Cycling is ten times more important than electric cars for reaching net-zero cities](https://theconversation.com/cycling-is-ten-times-more-important-than-electric-cars-for-reaching-net-zero-cities-157163) (un vulgarisation de cet article scientifique: Brand, Christian, et al. « [The climate change mitigation effects of daily active travel in cities.](https://www.sciencedirect.com/science/article/pii/S1361920921000687?via%3Dihub) » _Transportation Research Part D: Transport and Environment_ 93 (2021): 102764.)
[How green is cycling? Riding, walking, ebikes and driving ranked](https://www.bikeradar.com/features/long-reads/cycling-environmental-impact/)
- de Blas, Ignacio, et al. « [The limits of transport decarbonization under the current growth paradigm.](https://www.sciencedirect.com/science/article/pii/S2211467X20300961) » _Energy Strategy Reviews_ 32 (2020): 100543.

### Risques des batteries lithium ion (un sujet que je n’ai pas abordé)

- [Voiture électrique, risques incendies](https://www.automobile-propre.com/dossiers/voiture-electrique-risques-incendies/).

### Thèse d'Aurélien Bigo

- [Les transports face au défi de la transition énergétique. Explorations entre passé et avenir, technologie et sobriété, accélération et ralentissement](http://www.chair-energy-prosperity.org/wp-content/uploads/2019/01/These-Aurelien-Bigo.pdf).

### Autres analyses du cycle de vie sur le sujet des voitures électriques

- Helmers, Eckard, Johannes Dietz, and Martin Weiss. [Sensitivity Analysis in the Life-Cycle Assessment of Electric vs. Combustion Engine Cars under Approximate Real-World Conditions.](https://www.mdpi.com/2071-1050/12/3/1241) _Sustainability_ 12.3 (2020): 1241.
- Ambrose, H., & Kendall, A. (2016). [Effects of battery chemistry and performance on the life cycle greenhouse gas intensity of electric mobility](https://trid.trb.org/view/1417967). Transportation Research Part D: Transport and Environment, 47, 182-194.
- Bauer, C., Hofer, J., Althaus, H. J., Del Duce, A., & Simons, A. (2015). [The environmental performance of current and future passenger vehicles: Life cycle assessment based on a novel scenario analysis framework](https://econpapers.repec.org/article/eeeappene/v_3a157_3ay_3a2015_3ai_3ac_3ap_3a871-883.htm). Applied energy, 157, 871-883.
- Cox, B., Mutel, C. L., Bauer, C., Mendoza Beltran, A., & van Vuuren, D. P. (2018). [Uncertain environmental footprint of current and future battery electric vehicles](https://pubmed.ncbi.nlm.nih.gov/29570287/). Environmental science & technology, 52(8), 4989-4995.
- Ellingsen, L. A. W., Singh, B., & Strømman, A. H. (2016). [The size and range effect: lifecycle greenhouse gas emissions of electric vehicles](https://iopscience.iop.org/article/10.1088/1748-9326/11/5/054010). Environmental Research Letters, 11(5), 054010.
- [Life Cycle Analysis of the Climate Impact of Electric Vehicles](https://www.transportenvironment.org/sites/te/files/publications/TE%20-%20draft%20report%20v04.pdf). - Transport & Environment  
- Miotti, M., Hofer, J., & Bauer, C. (2017). [Integrated environmental and economic assessment of current and future fuel cell vehicles](https://link.springer.com/article/10.1007/s11367-015-0986-4). The International Journal of Life Cycle Assessment, 22(1), 94-110.
- Nordelöf, A., Messagie, M., Tillman, A. M., Söderman, M. L., & Van Mierlo, J. (2014). [Environmental impacts of hybrid, plug-in hybrid, and battery electric vehicles—what can we learn from life cycle assessment?](https://link.springer.com/article/10.1007/s11367-014-0788-0). The International Journal of Life Cycle Assessment, 19(11), 1866-1890.
- PSI/EMPA/ETHZ. (2016). [Thelma Project: Opportunities and challenges for electric mobility: an interdisciplinary assessment of passenger vehicles](https://www.psi.ch/lea/HomeEN/Final-Report-THELMA-Project.pdf).
- Peters, J. F., Baumann, M., Zimmermann, B., Braun, J., & Weil, M. (2017). [The environmental impact of Li-Ion batteries and the role of key parameters–A review](https://www.sciencedirect.com/science/article/abs/pii/S1364032116304713). Renewable and Sustainable Energy Reviews, 67, 491-506.

### Autres

- [Malus poids, émissions de CO₂ : intéressons-nous enfin aux véhicules intermédiaires !](https://theconversation.com/malus-poids-emissions-de-co-interessons-nous-enfin-aux-vehicules-intermediaires-148650) - The Conversation (article dont j’ai tiré l’image montrant pleins de petits véhicules).
- Source de la [photo où on voit la place que prend un bus, des vélos et des voitures](https://www.flickr.com/photos/danielbowen/7999510360/).
- [Quel carburant émet le plus de CO2, l’essence ou le gasoil ?](https://www.futura-sciences.com/planete/questions-reponses/automobile-carburant-emet-plus-co2-essence-gasoil-947/) - Futura-sciences  
- [Production électrique de l’éolien en 2020](http://www.journal-eolien.org/tout-sur-l-eolien/la-puissance-eolienne-en-france/) (39,7 TWh)
- [Évolution de la production électrique en France depuis 1980](https://www.eia.gov/international/data/world/electricity/electricity-generation?pd=2&p=00000000000000000000000000000fvu&u=1&f=A&v=line&a=-&i=none&vo=value&t=C&g=none&l=249--77&s=315532800000&e=1546300800000&vb=146&ev=true).
- [Immatriculations voitures électriques](http://www.avere-france.org/Site/Category/?arborescence_id=247) - Avere
- [Immatriculation des voitures électriques en France](https://fr.wikipedia.org/wiki/Voiture_%C3%A9lectrique_en_France#/media/Fichier:EV_Registrations_France_2010_2013.png).
- Un debunk d’une désinformation qui avait pas mal tourné sur l’impact des voitures électriques. [Comparing the lifetime green house gas emissions of electric cars with the emissions of cars using gasoline or diesel](https://www.oliver-krischer.eu/wp-content/uploads/2020/08/English_Studie.pdf).
- L’électrique ailleurs: [Uganda: E-mobility on a pay as you go model to boost sector](https://www.esi-africa.com/industry-sectors/future-energy/uganda-e-mobility-on-a-pay-as-you-go-model-to-boost-sector/).
- France Stratégie: [Comment faire enfin baisser les émissions de CO2 des voitures](https://www.strategie.gouv.fr/publications/faire-enfin-baisser-emissions-de-co2-voitures).
- [Voiture électrique : les utilisateurs satisfaits de leur investissement](https://www.automobile-propre.com/voiture-electrique-utilisateurs-satisfaits-investissement/)
