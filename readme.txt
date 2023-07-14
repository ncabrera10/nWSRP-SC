Nicolas Cabrera
Checker and evaluator of solutions for the WSRP

-------------------------------------------------------------------------------------------------------------
If you want to use this code:
1. Check that all the instances files from Kovacs et al. (2012) in txt format are on the "instances" folder.
2. Add all your solutions to the "solutions" folder. 
Your solutions should have the format: AlgorithmName_NumNodes_NumTechs_VersionTechs_MaxPerTeam_Instance_skills_VersionTeams.txt
Where:
	-NumNodes: 25,50,100
	-NumTechs: -1 -> all of them, otherwise select the number you want..
	-VersionTechs: Complete or Reduced
	-MaxPerTeam: 1,2,3,4
	-Instance: C101, C103, C201, C203, R101, R103, R201, R203, RC101, RC103, RC201, RC203
	-VersionTeam: Team or noTeam

For example: "BPC_25_90_Complete_1_C101_5x4_Team.txt"

3. Open the class "Main" in the "main" package.
4. Run as a Java Application.

All your results and reports will be store on the "results" folder.

----------------------------------------------------------------------------------------------------------

Parameters regarding the maximum walking distance, the maximum walking distance between two nodes and so on..
can be changed in the parametersGlobal.xml file in the config folder.

----------------------------------------------------------------------------------------------------------

This code is a work in progress. More constraints can be added in the "constraints package".
