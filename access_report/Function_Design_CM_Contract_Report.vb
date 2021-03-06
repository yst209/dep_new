Option Compare Database

Function Report_Contract_Form_By_Portfolio_Manager(UserPath, selectedPM)
    'DoCmd.OpenReport "RPT:Substential_Complete_Summary", acViewPreview, , , acWindowNormal
    Dim appExcel As Excel.Application
    Dim wbk As Excel.Workbook
    Dim wks As Excel.Worksheet
    Dim sTemplate As String
    
    Dim db As Database
    Dim RS As DAO.Recordset


        
    DoCmd.Hourglass True
    
    Dim PorfolioManager As String
    PorfolioManager = selectedPM
    
    
    sTemplate = "C:\Users\Vincent\Documents\BEDC DB Report\template.xlsx"
    sOutput = UserPath & "\DESIGN_CM_CONTRACT_REPORT_" & PorfolioManager & ".xlsx"
   
    If Dir(sOutput) <> "" Then Kill sOutput
    FileCopy sTemplate, sOutput
    
    Set appExcel = Excel.Application
    Set wbk = appExcel.Workbooks.Open(sOutput)
    Set wks = wbk.Worksheets(1)
    
    appExcel.Application.DisplayAlerts = False 'disable alert


    Dim iRow As Integer
    Set db = CurrentDb
    Dim sSQL As String
    sSQL = "SELECT * FROM Q_Design_Contract_Report"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)

    
    
    iRow = 5 
    wks.Cells(1, "A") = PorfolioManager
	
    Call Contract_Report_By_Portfolio_Manager(UserPath, iRow, wbk, wks, RS, "Design", PorfolioManager)


    

    RS.MoveFirst
	
    iRow = iRow + 10
	Rows(iRow).RowHeight = 30
	Rows(iRow).Font.Size = 20
	Rows(iRow).Font.Bold = True
	wks.Cells(iRow, "A") = "CM CONTRACT REPORT (DRAFT)"
	Set_Borders ("A" & iRow & ":C" & iRow)
	
	iRow = iRow + 1
	Range("A" & iRow & ":AC" & iRow).HorizontalAlignment = xlCenter
	Range("A" & iRow & ":AC" & iRow).Font.Size = 14
	Range("A" & iRow & ":AC" & iRow).Font.Bold = True
	Set_Borders ("A" & iRow & ":I" & iRow)
	Set_Borders ("J" & iRow & ":L" & iRow)
	Set_Borders ("M" & iRow & ":AC" & iRow)
	wks.Cells(iRow, "K") = "BEDC Tracking Database"
	wks.Cells(iRow, "V") = "PMIS"
	Range("A" & iRow & ":I" & iRow).Merge
	Range("J" & iRow & ":L" & iRow).Merge
	Range("M" & iRow & ":AC" & iRow).Merge

	
	iRow = iRow + 1
    Rows(iRow).RowHeight = 80
	wks.Cells(iRow, "A") = "Contract ID"
	wks.Cells(iRow, "B") = "Project ID"
	wks.Cells(iRow, "C") = "Project Description"
	wks.Cells(iRow, "D") = "Status"
	wks.Cells(iRow, "E") = "Stage"
	wks.Cells(iRow, "F") = "Data Date"
	wks.Cells(iRow, "G") = "Accountable Manager"
	wks.Cells(iRow, "H") = "Portfolio Manager"
	wks.Cells(iRow, "I") = "WBS Phase"
	wks.Cells(iRow, "J") = "Current      Contract Value CCV"
	wks.Cells(iRow, "K") = "Original      Contract Value OCV"
	wks.Cells(iRow, "L") = "Current Un-allocated Contract Value       CUCV = CCV-C"
	wks.Cells(iRow, "M") = "Current Budget"
	wks.Cells(iRow, "N") = "Proposed Budget"
	wks.Cells(iRow, "O") = "Original            Contract Price              A"
	wks.Cells(iRow, "P") = "Registered      Change Order                          B"
	wks.Cells(iRow, "Q") = "Adjusted    Contract Price                               C = A+B"
	wks.Cells(iRow, "R") = "Pending      Change Order                     D"
	wks.Cells(iRow, "S") = "Current         Budget Forecast                            E = C+D"
	wks.Cells(iRow, "T") = "Amount Paid      to Date                            F"
	wks.Cells(iRow, "U") = "Work Completed Not Paid                        G"
	wks.Cells(iRow, "V") = "% Incurred Based on Budget Forecast                    H = (F+G)/ E"
	wks.Cells(iRow, "W") = "Physical % Complete                   I"
	wks.Cells(iRow, "X") = "Estimate to Complete (ETC)                          J"
	wks.Cells(iRow, "Y") = "Estimate At Completion (EAC)        K=F+G+J"
	wks.Cells(iRow, "Z") = "Projected Expenditures for Next 12 Months                  K'"
	wks.Cells(iRow, "AA") = "Contract Price Surplus/(Deficit) at Completion                                   L=C-K"
	wks.Cells(iRow, "AB") = "Forecast Budget Surplus/(Deficit) at Completion                           M=E-K"
	wks.Cells(iRow, "AC") = "Forecast Contract Surplus/(Deficit) in Next 12 Months              N=C-(F+G+K')"
	
	Range("A" & iRow & ":AC" & iRow).Font.Color = RGB(255, 255, 255)
	Range("A" & iRow & ":AC" & iRow).Interior.Color = RGB(97, 151, 217)
    Range("A" & iRow & ":AC" & iRow).Font.Size = 11
	Range("A" & iRow & ":AC" & iRow).Font.Bold = True
	Range("A" & iRow & ":AC" & iRow).HorizontalAlignment = xlCenter
	Range("A" & iRow & ":AC" & iRow).VerticalAlignment = xlCenter
	Range("A" & iRow & ":AC" & iRow).WrapText =true
	Set_Inside_Borders ("A" & iRow & ":AC" & iRow)

	Call Contract_Report_By_Portfolio_Manager(UserPath, iRow, wbk, wks, RS, "CM", PorfolioManager)

    RS.Close
    
    
    
    
    Cells(2, 1).Select
    wbk.Save
    wbk.Close True 'False
    appExcel.Application.DisplayAlerts = True 'enable alert
    appExcel.Quit
    
    
    'MsgBox ([Form_FRM:Governance Report].FileNaming.Column(0))
    
    NewName = UserPath & "\DESIGN_CM_CONTRACT_REPORT_" & PorfolioManager & ".xlsx"
    Name sOutput As NewName
End Function

Function Contract_Report_By_Portfolio_Manager(UserPath, iRow, wbk, wks, RS, contract_type, portfolio_manager)

    Dim initial_iRow, i, contract_type_matched As Integer
        
    initial_iRow = iRow
    Dim previousProjectID As String
    Dim previousResourceID As String
    Dim previousPhase As String
    Dim previousContractStartPoint As String
    Dim currentContractStartPoint As String
    
	
    Do Until RS.EOF
        If previousResourceID <> RS.Fields("Resource") Then 'Only retrieve contract level information when a new contract comes
            contractValues = Get_Contract_Value(RS.Fields("Resource"))
            printContract = Print_Contract(RS.Fields("Resource"), portfolio_manager)
			
			' CM = CM  Or  Design = D/P
			If(contract_type = contractValues(5) Or (contract_type = "Design" And (contractValues(5) = "D" Or contractValues(5) = "P"))) Then
				contract_type_matched = 1
			Else
				contract_type_matched = 0
			End If

        End If
        'Only go through those contracts that matches desired contract type, have KStatus = OPEN/CLOSED, and the right porfolio manager
        If (contract_type_matched = 1 And (contractValues(4) = "OPEN" Or contractValues(4) = "CLOSED") And printContract = 1) Then
            If (previousProjectID <> RS.Fields("Project_ID") Or previousPhase <> RS.Fields("PType") Or previousResourceID <> RS.Fields("Resource")) Then 'Give it a new row while seeing either new project or new phase
                iRow = iRow + 1
                
                
                If previousResourceID <> RS.Fields("Resource") Then 'If it is also under a new contract, set contract level information
                    
                    wks.Cells(iRow, "A") = contractValues(1)
                    wks.Cells(iRow, "J") = contractValues(3) 'Current Contract Value (CCV)
                    wks.Cells(iRow, "K") = contractValues(2) 'Original Contract Value (OCV)
                    Range("A" & iRow & ":AC" & iRow).Interior.Color = RGB(197, 217, 241)
                    currentContractStartPoint = iRow
                    iRow = iRow + 1
                End If
                
                
                
                'Fill up current row
                If previousResourceID = RS.Fields("Resource") And previousProjectID = RS.Fields("Project_ID") And previousPhase <> RS.Fields("PType") Then
                    'Conditional formatting
                    'Don't display duplicate contract and project names
                Else
					If previousResourceID = RS.Fields("Resource") And previousProjectID <> RS.Fields("Project_ID") Then
						'Conditional formatting
						'Don't display duplicate contract names
					Else
						wks.Cells(iRow, "A") = RS.Fields("Resource")
					End If
                    
                    wks.Cells(iRow, "B") = RS.Fields("Project_ID")
                    wks.Cells(iRow, "C") = RS.Fields("Project_Name")
                    wks.Cells(iRow, "D") = RS.Fields("Current_Status")
                    wks.Cells(iRow, "E") = RS.Fields("Current_Stage")
                    wks.Cells(iRow, "F") = RS.Fields("Data_Date")
                    
                    Select Case RS.Fields("Current_Stage")
                        Case "Con", "SC", "FC":
                            wks.Cells(iRow, "G") = RS.Fields("Accountable_Construction_Manager")
                        Case Else:
                            wks.Cells(iRow, "G") = RS.Fields("Accountable_Design_Manager")
                    End Select
                    
                    wks.Cells(iRow, "H") = RS.Fields("Portfolio_Manager")
                    
                End If
                
                wks.Cells(iRow, "I") = RS.Fields("PType")
                
                Select Case RS.Fields("PType")
                    Case "10":
                        wks.Cells(iRow, "I") = "10. FACILITY PLANNING"
                    Case "25":
                        wks.Cells(iRow, "I") = "25. DESIGN PROCUREMENT"
                    Case "30":
                        wks.Cells(iRow, "I") = "30. DESIGN"
                    Case "40":
                        wks.Cells(iRow, "I") = "40. CONSTRUCTION PROCUREMENT"
                    Case "50":
                        wks.Cells(iRow, "I") = "50. CONSTRUCTION"
                    Case "60":
                        wks.Cells(iRow, "I") = "60. CLOSEOUT"
                End Select
            End If
			
			'The number should be added up to contract level summary for each record
			Select Case RS.Fields("Cost Set")
				Case "Budget":
					wks.Cells(iRow, "M") = wks.Cells(iRow, "M") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "M") = wks.Cells(currentContractStartPoint, "M") + RS.Fields("Value")
				Case "ORIGCONT":
					wks.Cells(iRow, "O") = wks.Cells(iRow, "O") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "O") = wks.Cells(currentContractStartPoint, "O") + RS.Fields("Value")
				Case "RCO":
					wks.Cells(iRow, "P") = wks.Cells(iRow, "P") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "P") = wks.Cells(currentContractStartPoint, "P") + RS.Fields("Value")
				Case "PCO":
					wks.Cells(iRow, "R") = wks.Cells(iRow, "R") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "R") = wks.Cells(currentContractStartPoint, "R") + RS.Fields("Value")
				Case "Actual":
					wks.Cells(iRow, "T") = wks.Cells(iRow, "T") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "T") = wks.Cells(currentContractStartPoint, "T") + RS.Fields("Value")
				Case "WCNP":
					wks.Cells(iRow, "U") = wks.Cells(iRow, "U") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "U") = wks.Cells(currentContractStartPoint, "U") + RS.Fields("Value")
				Case "ETC":
					wks.Cells(iRow, "X") = wks.Cells(iRow, "X") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "X") = wks.Cells(currentContractStartPoint, "X") + RS.Fields("Value")
				Case "EAC":
					wks.Cells(iRow, "Y") = wks.Cells(iRow, "Y") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "Y") = wks.Cells(currentContractStartPoint, "Y") + RS.Fields("Value")
				Case "12MTH-FCAST":
					wks.Cells(iRow, "Z") = wks.Cells(iRow, "Z") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "Z") = wks.Cells(currentContractStartPoint, "Z") + RS.Fields("Value")
			End Select
			
        End If
        
        previousResourceID = RS.Fields("Resource")
        previousProjectID = RS.Fields("Project_ID")
        previousPhase = RS.Fields("PType")
        RS.MoveNext
    Loop
    
    '*******************************************************************************************
	
	Set_Inside_Borders ("A" & initial_iRow+1 & ":AC" & iRow)
    
    i = initial_iRow + 1
    Do Until i > iRow 'Boundary condition will not be executed
        wks.Cells(i, "Q") = wks.Cells(i, "O") + wks.Cells(i, "P") 'Adjusted Contract Price C = A+B
        wks.Cells(i, "S") = wks.Cells(i, "Q") + wks.Cells(i, "R") 'Current Budget Forecast E = C+D
        
        If wks.Cells(i, "S") <> 0 Then
            wks.Cells(i, "V") = (wks.Cells(i, "T") + wks.Cells(i, "U")) / wks.Cells(i, "S") '% Incurred Based on Budget Forecast H = (F+G)/ E
        Else
            wks.Cells(i, "V") = 0
        End If
        
        wks.Cells(i, "Y") = wks.Cells(i, "T") + wks.Cells(i, "U") + wks.Cells(i, "X") 'Estimate At Completion (EAC) K=F+G+J
        
        If wks.Cells(i, "Y") <> 0 Then
            wks.Cells(i, "W") = (wks.Cells(i, "T") + wks.Cells(i, "U")) / wks.Cells(i, "Y")   'Temporary solution Physical % Complete I=(F+G/K)
        Else
            wks.Cells(i, "W") = 0
        End If
        
        wks.Cells(i, "AA") = wks.Cells(i, "Q") - wks.Cells(i, "Y") 'Contract Price Surplus/(Deficit) at Completion L=C-K
        wks.Cells(i, "AB") = wks.Cells(i, "S") - wks.Cells(i, "Y") 'Forecast Budget Surplus/(Deficit) at Completion M=E-K
        wks.Cells(i, "AC") = wks.Cells(i, "Q") - (wks.Cells(i, "T") + wks.Cells(i, "U") + wks.Cells(i, "Z")) 'Forecast Contract Surplus/(Deficit) in Next 12 Months N=C-(F+G+K')
        
        If wks.Cells(i, "J") <> 0 Then 'when J is not null
            wks.Cells(i, "L") = wks.Cells(i, "J") - wks.Cells(i, "Q")   'Contract level Un-allocated amount
        End If
        
        i = i + 1
    Loop

    

End Function










Function Report_Contract_Form_By_Program(UserPath, selectedProgram)
    Dim appExcel As Excel.Application
    Dim wbk As Excel.Workbook
    Dim wks As Excel.Worksheet
    Dim sTemplate As String
    
    Dim db As Database
    Dim RS As DAO.Recordset

    DoCmd.Hourglass True
    
    
    sTemplate = "C:\Users\Vincent\Documents\BEDC DB Report\template.xlsx"
    sOutput = UserPath & "\DESIGN_CM_CONTRACT_REPORT_" & selectedProgram & ".xlsx"
   
    If Dir(sOutput) <> "" Then Kill sOutput
    FileCopy sTemplate, sOutput
    
    Set appExcel = Excel.Application
    Set wbk = appExcel.Workbooks.Open(sOutput)
    Set wks = wbk.Worksheets(1)
    
    appExcel.Application.DisplayAlerts = False 'disable alert


    Dim iRow As Integer
    Set db = CurrentDb
    Dim sSQL As String
	
	If selectedProgram = "BOTH" Then
		sSQL = "SELECT * FROM Q_Design_Contract_Report"
		wks.Cells(1, "A") = "BEDC Capital Program"
	ElseIf selectedProgram = "WWC" Then
		sSQL = "SELECT * FROM Q_Design_Contract_Report WHERE Master_Program = '" & selectedProgram & "'"
		wks.Cells(1, "A") = "Waste Water Capital Program"
	ElseIf selectedProgram = "WSC" Then
		sSQL = "SELECT * FROM Q_Design_Contract_Report WHERE Master_Program = '" & selectedProgram & "'"
		wks.Cells(1, "A") = "Water Supply Capital Program"
	Else 'Should not go here
		appExcel.Application.DisplayAlerts = True 'enable alert
		appExcel.Quit
		MsgBox ("Program Not Found.")
		Docmd.Quit
	End If
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)

    
    
    iRow = 5 
    Call Contract_Report(UserPath, iRow, wbk, wks, RS, "Design")

    

    RS.MoveFirst
	
    iRow = iRow + 10
	Rows(iRow).RowHeight = 30
	Rows(iRow).Font.Size = 20
	Rows(iRow).Font.Bold = True
	wks.Cells(iRow, "A") = "CM CONTRACT REPORT (DRAFT)"
	Set_Borders ("A" & iRow & ":C" & iRow)
	
	iRow = iRow + 1
	Range("A" & iRow & ":AC" & iRow).HorizontalAlignment = xlCenter
	Range("A" & iRow & ":AC" & iRow).Font.Size = 14
	Range("A" & iRow & ":AC" & iRow).Font.Bold = True
	Set_Borders ("A" & iRow & ":I" & iRow)
	Set_Borders ("J" & iRow & ":L" & iRow)
	Set_Borders ("M" & iRow & ":AC" & iRow)
	wks.Cells(iRow, "K") = "BEDC Tracking Database"
	wks.Cells(iRow, "V") = "PMIS"
	Range("A" & iRow & ":I" & iRow).Merge
	Range("J" & iRow & ":L" & iRow).Merge
	Range("M" & iRow & ":AC" & iRow).Merge

	
	iRow = iRow + 1
    Rows(iRow).RowHeight = 80
	wks.Cells(iRow, "A") = "Contract ID"
	wks.Cells(iRow, "B") = "Project ID"
	wks.Cells(iRow, "C") = "Project Description"
	wks.Cells(iRow, "D") = "Status"
	wks.Cells(iRow, "E") = "Stage"
	wks.Cells(iRow, "F") = "Data Date"
	wks.Cells(iRow, "G") = "Accountable Manager"
	wks.Cells(iRow, "H") = "Portfolio Manager"
	wks.Cells(iRow, "I") = "WBS Phase"
	wks.Cells(iRow, "J") = "Current      Contract Value CCV"
	wks.Cells(iRow, "K") = "Original      Contract Value OCV"
	wks.Cells(iRow, "L") = "Current Un-allocated Contract Value       CUCV = CCV-C"
	wks.Cells(iRow, "M") = "Current Budget"
	wks.Cells(iRow, "N") = "Proposed Budget"
	wks.Cells(iRow, "O") = "Original            Contract Price              A"
	wks.Cells(iRow, "P") = "Registered      Change Order                          B"
	wks.Cells(iRow, "Q") = "Adjusted    Contract Price                               C = A+B"
	wks.Cells(iRow, "R") = "Pending      Change Order                     D"
	wks.Cells(iRow, "S") = "Current         Budget Forecast                            E = C+D"
	wks.Cells(iRow, "T") = "Amount Paid      to Date                            F"
	wks.Cells(iRow, "U") = "Work Completed Not Paid                        G"
	wks.Cells(iRow, "V") = "% Incurred Based on Budget Forecast                    H = (F+G)/ E"
	wks.Cells(iRow, "W") = "Physical % Complete                   I"
	wks.Cells(iRow, "X") = "Estimate to Complete (ETC)                          J"
	wks.Cells(iRow, "Y") = "Estimate At Completion (EAC)        K=F+G+J"
	wks.Cells(iRow, "Z") = "Projected Expenditures for Next 12 Months                  K'"
	wks.Cells(iRow, "AA") = "Contract Price Surplus/(Deficit) at Completion                                   L=C-K"
	wks.Cells(iRow, "AB") = "Forecast Budget Surplus/(Deficit) at Completion                           M=E-K"
	wks.Cells(iRow, "AC") = "Forecast Contract Surplus/(Deficit) in Next 12 Months              N=C-(F+G+K')"
	
	Range("A" & iRow & ":AC" & iRow).Font.Color = RGB(255, 255, 255)
	Range("A" & iRow & ":AC" & iRow).Interior.Color = RGB(97, 151, 217)
    Range("A" & iRow & ":AC" & iRow).Font.Size = 11
	Range("A" & iRow & ":AC" & iRow).Font.Bold = True
	Range("A" & iRow & ":AC" & iRow).HorizontalAlignment = xlCenter
	Range("A" & iRow & ":AC" & iRow).VerticalAlignment = xlCenter
	Range("A" & iRow & ":AC" & iRow).WrapText =true
	Set_Inside_Borders ("A" & iRow & ":AC" & iRow)

	Call Contract_Report(UserPath, iRow, wbk, wks, RS, "CM")

    RS.Close
    
    
    
    
    Cells(2, 1).Select
    wbk.Save
    wbk.Close True 'False
    appExcel.Application.DisplayAlerts = True 'enable alert
    appExcel.Quit
    
    
    'MsgBox ([Form_FRM:Governance Report].FileNaming.Column(0))
    
    NewName = UserPath & "\DESIGN_CM_CONTRACT_REPORT_" & selectedProgram & ".xlsx"
    Name sOutput As NewName
End Function

Function Contract_Report(UserPath, iRow, wbk, wks, RS, contract_type)

    Dim initial_iRow, i, contract_type_matched As Integer
        
    initial_iRow = iRow
    Dim previousProjectID As String
    Dim previousResourceID As String
    Dim previousPhase As String
    Dim previousContractStartPoint As String
    Dim currentContractStartPoint As String
    
	
    Do Until RS.EOF
        If previousResourceID <> RS.Fields("Resource") Then 'Only retrieve contract level information when a new contract comes
            contractValues = Get_Contract_Value(RS.Fields("Resource"))
			
			' CM = CM  Or  Design = D/P
			If(contract_type = contractValues(5) Or (contract_type = "Design" And (contractValues(5) = "D" Or contractValues(5) = "P"))) Then
				contract_type_matched = 1
			Else
				contract_type_matched = 0
			End If

        End If
        'Only go through those contracts that matches desired contract type, have KStatus = OPEN/CLOSED, and the right porfolio manager
        If (contract_type_matched = 1 And (contractValues(4) = "OPEN" Or contractValues(4) = "CLOSED")) Then
            If (previousProjectID <> RS.Fields("Project_ID") Or previousPhase <> RS.Fields("PType") Or previousResourceID <> RS.Fields("Resource")) Then 'Give it a new row while seeing either new project or new phase
                iRow = iRow + 1
                
                
                If previousResourceID <> RS.Fields("Resource") Then 'If it is also under a new contract, set contract level information
                    
                    wks.Cells(iRow, "A") = contractValues(1)
                    wks.Cells(iRow, "J") = contractValues(3) 'Current Contract Value (CCV)
                    wks.Cells(iRow, "K") = contractValues(2) 'Original Contract Value (OCV)
                    Range("A" & iRow & ":AC" & iRow).Interior.Color = RGB(197, 217, 241)
                    currentContractStartPoint = iRow
                    iRow = iRow + 1
                End If
                
                
                
                'Fill up current row
                If previousResourceID = RS.Fields("Resource") And previousProjectID = RS.Fields("Project_ID") And previousPhase <> RS.Fields("PType") Then
                    'Conditional formatting
                    'Don't display duplicate contract and project names
                Else
					If previousResourceID = RS.Fields("Resource") And previousProjectID <> RS.Fields("Project_ID") Then
						'Conditional formatting
						'Don't display duplicate contract names
					Else
						wks.Cells(iRow, "A") = RS.Fields("Resource")
					End If
                    
                    wks.Cells(iRow, "B") = RS.Fields("Project_ID")
                    wks.Cells(iRow, "C") = RS.Fields("Project_Name")
                    wks.Cells(iRow, "D") = RS.Fields("Current_Status")
                    wks.Cells(iRow, "E") = RS.Fields("Current_Stage")
                    wks.Cells(iRow, "F") = RS.Fields("Data_Date")
                    
                    Select Case RS.Fields("Current_Stage")
                        Case "Con", "SC", "FC":
                            wks.Cells(iRow, "G") = RS.Fields("Accountable_Construction_Manager")
                        Case Else:
                            wks.Cells(iRow, "G") = RS.Fields("Accountable_Design_Manager")
                    End Select
                    
                    wks.Cells(iRow, "H") = RS.Fields("Portfolio_Manager")
                    
                End If
                
                wks.Cells(iRow, "I") = RS.Fields("PType")
                
                Select Case RS.Fields("PType")
                    Case "10":
                        wks.Cells(iRow, "I") = "10. FACILITY PLANNING"
                    Case "25":
                        wks.Cells(iRow, "I") = "25. DESIGN PROCUREMENT"
                    Case "30":
                        wks.Cells(iRow, "I") = "30. DESIGN"
                    Case "40":
                        wks.Cells(iRow, "I") = "40. CONSTRUCTION PROCUREMENT"
                    Case "50":
                        wks.Cells(iRow, "I") = "50. CONSTRUCTION"
                    Case "60":
                        wks.Cells(iRow, "I") = "60. CLOSEOUT"
                End Select
            End If
			
			'The number should be added up to contract level summary for each record
			Select Case RS.Fields("Cost Set")
				Case "Budget":
					wks.Cells(iRow, "M") = wks.Cells(iRow, "M") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "M") = wks.Cells(currentContractStartPoint, "M") + RS.Fields("Value")
				Case "ORIGCONT":
					wks.Cells(iRow, "O") = wks.Cells(iRow, "O") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "O") = wks.Cells(currentContractStartPoint, "O") + RS.Fields("Value")
				Case "RCO":
					wks.Cells(iRow, "P") = wks.Cells(iRow, "P") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "P") = wks.Cells(currentContractStartPoint, "P") + RS.Fields("Value")
				Case "PCO":
					wks.Cells(iRow, "R") = wks.Cells(iRow, "R") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "R") = wks.Cells(currentContractStartPoint, "R") + RS.Fields("Value")
				Case "Actual":
					wks.Cells(iRow, "T") = wks.Cells(iRow, "T") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "T") = wks.Cells(currentContractStartPoint, "T") + RS.Fields("Value")
				Case "WCNP":
					wks.Cells(iRow, "U") = wks.Cells(iRow, "U") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "U") = wks.Cells(currentContractStartPoint, "U") + RS.Fields("Value")
				Case "ETC":
					wks.Cells(iRow, "X") = wks.Cells(iRow, "X") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "X") = wks.Cells(currentContractStartPoint, "X") + RS.Fields("Value")
				Case "EAC":
					wks.Cells(iRow, "Y") = wks.Cells(iRow, "Y") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "Y") = wks.Cells(currentContractStartPoint, "Y") + RS.Fields("Value")
				Case "12MTH-FCAST":
					wks.Cells(iRow, "Z") = wks.Cells(iRow, "Z") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "Z") = wks.Cells(currentContractStartPoint, "Z") + RS.Fields("Value")
			End Select
			
        End If
        
        previousResourceID = RS.Fields("Resource")
        previousProjectID = RS.Fields("Project_ID")
        previousPhase = RS.Fields("PType")
        RS.MoveNext
    Loop
    
    '*******************************************************************************************
	
	Set_Inside_Borders ("A" & initial_iRow+1 & ":AC" & iRow)
    
    i = initial_iRow + 1
    Do Until i > iRow 'Boundary condition will not be executed
        wks.Cells(i, "Q") = wks.Cells(i, "O") + wks.Cells(i, "P") 'Adjusted Contract Price C = A+B
        wks.Cells(i, "S") = wks.Cells(i, "Q") + wks.Cells(i, "R") 'Current Budget Forecast E = C+D
        
        If wks.Cells(i, "S") <> 0 Then
            wks.Cells(i, "V") = (wks.Cells(i, "T") + wks.Cells(i, "U")) / wks.Cells(i, "S") '% Incurred Based on Budget Forecast H = (F+G)/ E
        Else
            wks.Cells(i, "V") = 0
        End If
        
        wks.Cells(i, "Y") = wks.Cells(i, "T") + wks.Cells(i, "U") + wks.Cells(i, "X") 'Estimate At Completion (EAC) K=F+G+J
        
        If wks.Cells(i, "Y") <> 0 Then
            wks.Cells(i, "W") = (wks.Cells(i, "T") + wks.Cells(i, "U")) / wks.Cells(i, "Y")   'Temporary solution Physical % Complete I=(F+G/K)
        Else
            wks.Cells(i, "W") = 0
        End If
        
        wks.Cells(i, "AA") = wks.Cells(i, "Q") - wks.Cells(i, "Y") 'Contract Price Surplus/(Deficit) at Completion L=C-K
        wks.Cells(i, "AB") = wks.Cells(i, "S") - wks.Cells(i, "Y") 'Forecast Budget Surplus/(Deficit) at Completion M=E-K
        wks.Cells(i, "AC") = wks.Cells(i, "Q") - (wks.Cells(i, "T") + wks.Cells(i, "U") + wks.Cells(i, "Z")) 'Forecast Contract Surplus/(Deficit) in Next 12 Months N=C-(F+G+K')
        
        If wks.Cells(i, "J") <> 0 Then 'when J is not null
            wks.Cells(i, "L") = wks.Cells(i, "J") - wks.Cells(i, "Q")   'Contract level Un-allocated amount
        End If
        
        i = i + 1
    Loop

    

End Function





Function Get_Contract_Value(Resource)
    Dim db As Database
    Dim RS As DAO.Recordset
    Dim tmpArray(0 To 5)
    Set db = CurrentDb
    Dim test As String
    test = Replace(Resource, " ", "")
    
    sSQL = "SELECT * FROM Contract_Report_Value WHERE Replace(KNumber,' ','') = '" & Replace(Resource, " ", "") & "'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.RecordCount > 0 Then
            tmpArray(0) = RS.Fields("KNumber")
            tmpArray(1) = RS.Fields("KName")
            tmpArray(2) = RS.Fields("OrigAmount")
            tmpArray(3) = RS.Fields("CurrentAmount")
            tmpArray(4) = RS.Fields("KStatus")
            tmpArray(5) = RS.Fields("Contract_Type")
        Else
            tmpArray(0) = "N/A"
            tmpArray(1) = "N/A"
            tmpArray(2) = "$0.00"
            tmpArray(3) = "$0.00"
            'tmpArray(3) = "OPEN"
        End If
    RS.Close
    
    Get_Contract_Value = tmpArray
End Function


Function Print_Contract(Resource, Manager)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT Count(Q_Design_Contract_Report.Project_ID) AS NumOfProjects FROM Q_Design_Contract_Report WHERE Resource = '" & Resource & "' AND Portfolio_Manager = '" & Manager & "'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.Fields("NumOfProjects") > 0 Then
            Print_Contract = 1
        Else
            Print_Contract = 0
        End If
    RS.Close
    
End Function


Function Set_Inside_Borders(range_string)

    Dim rng    As Range
    On Error Resume Next

    Set rng = Range(range_string)
    On Error GoTo 0
    With rng.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlInsideVertical)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlInsideHorizontal)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With

    'clear the variable
    Set rng = Nothing
End Function

Function Set_Borders(range_string)

    Dim rng    As Range
    On Error Resume Next

    Set rng = Range(range_string)
    On Error GoTo 0
    With rng.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlInsideVertical)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With rng.Borders(xlInsideHorizontal)
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With

    'clear the variable
    Set rng = Nothing
End Function
