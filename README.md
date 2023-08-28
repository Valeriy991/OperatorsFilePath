# OperatorsFilePath
Project on the topic "Working with the file system in Java".
1. The program receives the name of the root directory where the "new_data" folder is located
2. The program should scan the network folder every 10 seconds and check for new files in it. The files have a name RE_FRAUD_LIST_yyyyMMdd_000000_00000.txt
 All new files should be processed in the following order:
• based on the existing files, generate new files and write them to the appropriate folder with the name of the OPERATOR located in the processed_data folder
• each new file must have the following name OPERATOR_FRAUD_LIST_yyyyMMdd_*.0.txt, where *.0 is the serial number of the file in the OPERATOR folder
• in each new file, information is entered from the corresponding file by date, ignoring the data in the columns of which NO_FRAUD is specified, grouping the data for each operator SEPARATELY
3. Move all processed files from the "new_data" folder to the "processed_data\processed" folder. If there are already files with the same names in the target folder,
4. then transfer the files under the new names according to the file name template (unit_name).txt
