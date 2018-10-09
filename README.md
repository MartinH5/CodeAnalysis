# CodeAnalysis

## PMD Results

PMD er en statisk code analyzer. Den finder hurtigt en masse foreslag til forbedringer. Den fandt blandt andet en lang række unused imports samt variabler. Den brokkede sig i øvrigt over anvendelsen af ArrayList som import og foreslog i stedet at bruge implementation af interfacet. Den gennemgik manglende exception håndtering osv. Kort sagt er det en statisk test analyzer, der bruges til at forbedre effektiviteten og læsbarheden. (Koden kan findes i dette repo, som "Gutenberg Project") 

10 ting, der bør checkes: 

* EmptyCatchBlock, hvis en exception fanges, så bør den håndteres korrekt. I nogen tilfælde er logging tilstrækkeligt 
* AvoidDeeplyNestedIfStmts. Det gør debugging og testing problematisk, samt kode der er sværere at vedligeholde. 
* CyclomaticComplexity. Ligeledes gør en høj cyclomatisk kompleksitet der sværere at teste og vedligeholde kode. 
* SwitchStmtsShouldHaveDefault. Efter min mening giver det god mening at have en default switch, hvis input ikke bliver accepteret. 
* UnconditionalIfStatement, undgå if statements, hvis conditions altid er true/false. 
* ForLoopShouldBeWhileLoop. I nogen tilfælde kan man med fordel at lave while- i stedet for for-loop, for mere kortfattet kode. 
* ReturnFromFinallyBlock. Man bør altid undgå at returne i en finally block, da det i mangen tilfælde vil kunne gå uden om catches. 
* CouplingBetweenObjects. Som udgangspunkt vil man gerne have kode med High Cohesion og Low Coupling.
* ExcessiveImports. For at mindske coupling og fjerne ligegyldigheder, så bør man undgå unødvendige imports.
* SystemPrintln. Print Line kan være brugbar, mens man tester enkelte moduler af ens kode, men ved logging er det man som regel bruger i produktion. 

Der er selvfølgelig en lang række rules som giver mening at have med, men jeg valgte 10 som jeg fandt relevante. 

## Profiling

Jeg har kørt profiling på et [projekt vedrørende sets](https://github.com/MartinH5/ProgrammingWithSets/), for at finde ud af, hvad der skaber eventuelle forsinkelser. 

![Profiling](https://raw.githubusercontent.com/MartinH5/CodeAnalysis/master/Udklip.PNG) "Profiling"
