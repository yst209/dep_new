Option Compare Database

Sub Copy_Paste_Table_Block(ByRef wbk As Excel.Workbook, ByRef wks As Excel.Worksheet, str As String, ByVal RowNum As Integer)
    Dim appExcel As Excel.Application
    Set appExcel = Excel.Application
    appExcel.Application.GoTo Reference:=str
    Selection.Copy
    Sheets("Report").Select
    wks.Cells(RowNum, 1).Select
    ActiveSheet.Paste
End Sub

Sub Cut_Paste_Table_Block(ByRef wbk As Excel.Workbook, ByRef wks As Excel.Worksheet, str As String, ByVal RowNum As Integer)
    Dim appExcel As Excel.Application
    Set appExcel = Excel.Application
    appExcel.Application.GoTo Reference:=str
    Selection.Cut
    Sheets("Report").Select
    wks.Cells(RowNum, 1).Select
    ActiveSheet.Paste
End Sub

Function Report_Baseline_Approval_Form(UserPath, UserPeriod, UserProjectID, UserProjectName)
    'DoCmd.OpenReport "RPT:Substential_Complete_Summary", acViewPreview, , , acWindowNormal
    Dim appExcel As Excel.Application
    Dim wbk As Excel.Workbook
    Dim wks As Excel.Worksheet
    Dim sTemplate As String
    
    Dim db As Database
    Dim RS As DAO.Recordset
    Dim sSQL As String
    Const SectionGap = 2
    Const TableGap = 1
    Dim iRow, iCol, i, j As Integer
        
    DoCmd.Hourglass True
    
    'Name the new file for today's date
    Dim TheDate As Date
    Dim strTime As String
    
    'Copy template to new file and start filling up new file
    'sTemplate = "\\dep-bee\bee\ProjectsMDE\FORMS\DEP-BEDC_Baseline_Revision_Approval_Form.xls"
    'sOutput = UserPath & "\DEP-BEDC_Baseline_Revision_Approval_Form_" & UserProjectName & ".xls"
    
    
    sTemplate = "C:\Users\Vincent\Documents\BEDC DB Report\template.xlsx"
    sOutput = UserPath & "\DESIGN_CONTRACT_REPORT.xlsx"
   
    If Dir(sOutput) <> "" Then Kill sOutput
    FileCopy sTemplate, sOutput
    
    Set appExcel = Excel.Application
    Set wbk = appExcel.Workbooks.Open(sOutput)
    Set wks = wbk.Worksheets(1)
    
    appExcel.Application.DisplayAlerts = False 'disable alert
    
    Set db = CurrentDb
    
    

    


    Cells(2, 1).Select
    wbk.Save
    wbk.Close True 'False
    appExcel.Application.DisplayAlerts = True 'enable alert
    appExcel.Quit
    
    
    Select Case [Form_FRM:Baseline_Revision_Approval_Form].FileNaming.Column(0)
        Case 0:
            tmp = ""
        Case 1:
            tmp = Governance_Report_Get_Personnel_From_P6(UserProjectID, "Accountable") & "_"
        Case 2:
            tmp = Governance_Report_Get_Personnel_From_P6(UserProjectID, "Portfolio") & "_"
    End Select
    'MsgBox ([Form_FRM:Governance Report].FileNaming.Column(0))
    
    NewName = UserPath & "\DESIGN_CONTRACT_REPORT.xlsx"
    Name sOutput As NewName
    
End Function


Function Report_Contract_Form(UserPath, selectedPM)
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
	
    Call Contract_Report(UserPath, iRow, wbk, wks, RS, "Design", PorfolioManager)


    

    RS.MoveFirst
	
    iRow = iRow + 10
	Rows(iRow).RowHeight = 30
	Rows(iRow).Font.Size = 20
	Rows(iRow).Font.Bold = True
	wks.Cells(iRow, "A") = "CM CONTRACT REPORT (DRAFT)"
	Set_Borders ("A" & iRow & ":C" & iRow)
	
	iRow = iRow + 1
	Range("A" & iRow & ":AA" & iRow).HorizontalAlignment = xlCenter
	Range("A" & iRow & ":AA" & iRow).Font.Size = 14
	Range("A" & iRow & ":AA" & iRow).Font.Bold = True
	Set_Borders ("A" & iRow & ":I" & iRow)
	Set_Borders ("J" & iRow & ":L" & iRow)
	Set_Borders ("M" & iRow & ":AA" & iRow)
	wks.Cells(iRow, "K") = "BEDC Tracking Database"
	wks.Cells(iRow, "T") = "PMIS"
	Range("A" & iRow & ":I" & iRow).Merge
	Range("J" & iRow & ":L" & iRow).Merge
	Range("M" & iRow & ":AA" & iRow).Merge

	
	iRow = iRow + 1
    Rows(iRow).RowHeight = 90
	wks.Cells(iRow, "A") = "Contract ID"
	wks.Cells(iRow, "B") = "Project ID"
	wks.Cells(iRow, "C") = "Project Description"
	wks.Cells(iRow, "D") = "Status"
	wks.Cells(iRow, "E") = "Stage"
	wks.Cells(iRow, "F") = "Data Date"
	wks.Cells(iRow, "G") = "Accountable Manager"
	wks.Cells(iRow, "H") = "Portfolio Manager"
	wks.Cells(iRow, "I") = "WBS Phase"
	wks.Cells(iRow, "J") = "Current Contract Value              (CCV)"
	wks.Cells(iRow, "K") = "Original Contract Value              (OCV)"
	wks.Cells(iRow, "L") = "Current Un-allocated Contract Value       CUCV = CCV-C"
	wks.Cells(iRow, "M") = "Original Contract Price                     (A)"
	wks.Cells(iRow, "N") = "Registered Change Order                          (B)"
	wks.Cells(iRow, "O") = "Adjusted Contract Price                               C = A+B"
	wks.Cells(iRow, "P") = "Pending Change Order                   D"
	wks.Cells(iRow, "Q") = "Current Budget Forecast                            E = C+D"
	wks.Cells(iRow, "R") = "Amount Paid to Date                            F"
	wks.Cells(iRow, "S") = "Work Completed Not Paid                        G"
	wks.Cells(iRow, "T") = "% Incurred Based on Budget Forecast                    H = (F+G)/ E"
	wks.Cells(iRow, "U") = "Physical % Complete                   I"
	wks.Cells(iRow, "V") = "Estimate to Complete (ETC)                          J"
	wks.Cells(iRow, "W") = "Estimate At Completion (EAC)        K=F+G+J"
	wks.Cells(iRow, "X") = "Projected Expenditures for Next 12 Months                  K'"
	wks.Cells(iRow, "Y") = "Contract Price Surplus/(Deficit) at Completion                                   L=C-K"
	wks.Cells(iRow, "Z") = "Forecast Budget Surplus/(Deficit) at Completion                           M=E-K"
	wks.Cells(iRow, "AA") = "Forecast Contract Surplus/(Deficit) in Next 12 Months              N=C-(F+G+K')"
	
	Range("A" & iRow & ":AA" & iRow).Font.Color = RGB(255, 255, 255)
	Range("A" & iRow & ":AA" & iRow).Interior.Color = RGB(97, 151, 217)
    Range("A" & iRow & ":AA" & iRow).Font.Size = 11
	Range("A" & iRow & ":AA" & iRow).Font.Bold = True
	Range("A" & iRow & ":AA" & iRow).HorizontalAlignment = xlCenter
	Range("A" & iRow & ":AA" & iRow).VerticalAlignment = xlCenter
	Range("A" & iRow & ":AA" & iRow).WrapText =true
	Set_Inside_Borders ("A" & iRow & ":AA" & iRow)

	Call Contract_Report(UserPath, iRow, wbk, wks, RS, "CM", PorfolioManager)

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


Function Contract_Report(UserPath, iRow, wbk, wks, RS, contract_type, portfolio_manager)

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
                    Range("A" & iRow & ":AA" & iRow).Interior.Color = RGB(197, 217, 241)
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
				Case "ORIGCONT":
					wks.Cells(iRow, "M") = wks.Cells(iRow, "M") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "M") = wks.Cells(currentContractStartPoint, "M") + RS.Fields("Value")
				Case "RCO":
					wks.Cells(iRow, "N") = wks.Cells(iRow, "N") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "N") = wks.Cells(currentContractStartPoint, "N") + RS.Fields("Value")
				Case "PCO":
					wks.Cells(iRow, "P") = wks.Cells(iRow, "P") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "P") = wks.Cells(currentContractStartPoint, "P") + RS.Fields("Value")
				Case "Actual":
					wks.Cells(iRow, "R") = wks.Cells(iRow, "R") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "R") = wks.Cells(currentContractStartPoint, "R") + RS.Fields("Value")
				Case "WCNP":
					wks.Cells(iRow, "S") = wks.Cells(iRow, "S") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "S") = wks.Cells(currentContractStartPoint, "S") + RS.Fields("Value")
				Case "ETC":
					wks.Cells(iRow, "V") = wks.Cells(iRow, "V") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "V") = wks.Cells(currentContractStartPoint, "V") + RS.Fields("Value")
				Case "EAC":
					wks.Cells(iRow, "W") = wks.Cells(iRow, "W") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "W") = wks.Cells(currentContractStartPoint, "W") + RS.Fields("Value")
				Case "12MTH-FCAST":
					wks.Cells(iRow, "X") = wks.Cells(iRow, "X") + RS.Fields("Value")
					wks.Cells(currentContractStartPoint, "X") = wks.Cells(currentContractStartPoint, "X") + RS.Fields("Value")
			End Select
			
        End If
        
        previousResourceID = RS.Fields("Resource")
        previousProjectID = RS.Fields("Project_ID")
        previousPhase = RS.Fields("PType")
        RS.MoveNext
    Loop
    
    '*******************************************************************************************
	
	Set_Inside_Borders ("A" & initial_iRow+1 & ":AA" & iRow)
    
    i = initial_iRow + 1
    Do Until i > iRow 'Boundary condition will not be executed
        wks.Cells(i, "O") = wks.Cells(i, "M") + wks.Cells(i, "N") 'Adjusted Contract Price C = A+B
        wks.Cells(i, "Q") = wks.Cells(i, "O") + wks.Cells(i, "P") 'Current Budget Forecast E = C+D
        
        If wks.Cells(i, "Q") <> 0 Then
            wks.Cells(i, "T") = (wks.Cells(i, "R") + wks.Cells(i, "S")) / wks.Cells(i, "Q") '% Incurred Based on Budget Forecast H = (F+G)/ E
        Else
            wks.Cells(i, "T") = 0
        End If
        
        wks.Cells(i, "W") = wks.Cells(i, "R") + wks.Cells(i, "S") + wks.Cells(i, "V") 'Estimate At Completion (EAC) K=F+G+J
        
        If wks.Cells(i, "W") <> 0 Then
            wks.Cells(i, "U") = (wks.Cells(i, "R") + wks.Cells(i, "S")) / wks.Cells(i, "W")   'Temporary solution Physical % Complete I=(F+G/K)
        Else
            wks.Cells(i, "U") = 0
        End If
        
        wks.Cells(i, "Y") = wks.Cells(i, "O") - wks.Cells(i, "W") 'Contract Price Surplus/(Deficit) at Completion L=C-K
        wks.Cells(i, "Z") = wks.Cells(i, "Q") - wks.Cells(i, "W") 'Forecast Budget Surplus/(Deficit) at Completion M=E-K
        wks.Cells(i, "AA") = wks.Cells(i, "O") - (wks.Cells(i, "R") + wks.Cells(i, "S") + wks.Cells(i, "X")) 'Forecast Contract Surplus/(Deficit) in Next 12 Months N=C-(F+G+K')
        
        If wks.Cells(i, "J") <> 0 Then 'when J is not null
            wks.Cells(i, "L") = wks.Cells(i, "J") - wks.Cells(i, "O")   'Contract level Un-allocated amount
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


Function Report_Governance(UserPath, UserPeriod, UserProjectID, UserProjectName)
    'DoCmd.OpenReport "RPT:Substential_Complete_Summary", acViewPreview, , , acWindowNormal
    Dim appExcel As Excel.Application
    Dim wbk As Excel.Workbook
    Dim wks As Excel.Worksheet
    Dim sTemplate As String
    
    Dim db As Database
    Dim RS As DAO.Recordset
    Dim sSQL As String
    Const SectionGap = 2
    Const TableGap = 1
    Dim iRow, iCol As Integer
    
    tmp1 = Governance_Report_Get_Personnel_From_P6(UserProjectID, "Portfolio")
    tmp2 = Governance_Report_Get_Personnel_From_P6(UserProjectID, "Accountable")
    'if specify a personnel
    If Not IsNull([Form_FRM:Governance Report].List3.Column(1)) Then
        'If Not IsNull(tmp1) And IsNull(tmp2) Then
            'Exit Function
        'End If
        If Not ([Form_FRM:Governance Report].List3.Column(1) = Nz(tmp1, 0) Or [Form_FRM:Governance Report].List3.Column(1) = Nz(tmp2, 0)) Then
            Exit Function
        End If
    End If
    
    
    DoCmd.Hourglass True
    
    'Name the new file for today's date
    Dim TheDate As Date
    Dim strTime As String
    
    'Copy template to new file and start filling up new file
    sTemplate = "\\dep-bee\bee\ProjectsMDE\FORMS\DEP-BEDC_Governance_Report_Template.xls"
    sOutput = UserPath & "\DEP-BEDC_Governance_Report_" & UserProjectName & "_" & UserPeriod & ".xls"
    If Dir(sOutput) <> "" Then Kill sOutput
    FileCopy sTemplate, sOutput
    
    Set appExcel = Excel.Application
    Set wbk = appExcel.Workbooks.Open(sOutput)
    Set wks = wbk.Worksheets(1)
    
    appExcel.Application.DisplayAlerts = False 'disable alert
    
    Set db = CurrentDb
    '*************************
    'Draw Header
    '*************************
    
    sSQL = "SELECT * FROM [Q_P6_Project_Schedule] WHERE [P6_Proj_id] = " & UserProjectID & " AND [Data_Period] = " & UserPeriod & " AND FB = 'F'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        
    'Get Last Achieved Milestone
    Do Until RS.EOF
        LastAchievedPhase = RS.Fields("LastA")
        RS.MoveNext
    Loop
    RS.Close
    
    sSQL = "SELECT * FROM [dbo_P6_Historical_Trend] WHERE [P6_Proj_id] = " & UserProjectID & " AND [Data_Period] = " & UserPeriod
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.RecordCount > 0 Then
            wks.Cells(1, 5) = RS.Fields("Project_ID")
            wks.Cells(2, 5) = RS.Fields("Project_Name")
            wks.Cells(3, 5) = Governance_Report_Get_Personnel_From_P6(UserProjectID, "Accountable")
            wks.Cells(1, 14) = MonthName(Right(UserPeriod, 2)) & " " & Left(UserPeriod, 4)
            'wks.Cells(2, 14) = Governance_Report_Get_Current_Phase(UserProjectID, UserPeriod)
            wks.Cells(2, 14) = Governance_Report_Get_Phase(LastAchievedPhase, 2)
            wks.Cells(3, 14) = RS.Fields("Current_Status")
        Else
            Exit Function
        End If
    RS.Close
    
    '*************************
    'Draw Contract Summary
    '*************************
    
    sSQL = "SELECT * FROM [Q_P6_Contract_Company] WHERE P6ID = " & UserProjectID & " AND [Project_ID] = '" & UserProjectName & "'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    Do Until RS.EOF
        'tmp = "[" & RS.Fields("KNumber") & "] " & RS.Fields("CName")
        
        Select Case RS.Fields("Contract_Type")
            Case "D", "P":
                wks.Cells(6, 2) = RS.Fields("KNumber")
                wks.Cells(6, 4) = RS.Fields("CName")
            Case "CM":
                wks.Cells(6, 11) = RS.Fields("KNumber")
                wks.Cells(6, 13) = RS.Fields("CName")
            Case "C":
                tmpCounter = tmpCounter + 1
                Select Case tmpCounter
                    Case 1: wks.Cells(7, 2) = RS.Fields("KNumber")
                            wks.Cells(7, 4) = RS.Fields("CName")
                    Case 2: wks.Cells(8, 2) = RS.Fields("KNumber")
                            wks.Cells(8, 4) = RS.Fields("CName")
                    Case 3: wks.Cells(7, 11) = RS.Fields("KNumber")
                            wks.Cells(7, 13) = RS.Fields("CName")
                    Case 4: wks.Cells(8, 11) = RS.Fields("KNumber")
                            wks.Cells(8, 13) = RS.Fields("CName")
                End Select
            Case Else:
        End Select
        RS.MoveNext
    Loop
    RS.Close
       
    '*************************
    'Draw EHS TABLE 1
    '*************************
    iRow = 14 + SectionGap
    Call Copy_Paste_Table_Block(wbk, wks, "EHS_1_H", iRow)
    iRow = iRow + 4
    
    'sSQL = "SELECT * FROM [Q_P6_Contract_Company] WHERE P6ID = " & UserProjectID
    sSQL = "SELECT * FROM [Q_P6_Contract_Company] WHERE P6ID = " & UserProjectID & " AND [Project_ID] = '" & UserProjectName & "'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    Do Until RS.EOF
        Select Case RS.Fields("Contract_Type")
            Case "C":
                tmp_Has_C_Contract = 1
                Call Copy_Paste_Table_Block(wbk, wks, "EHS_1_B", iRow)
                wks.Cells(iRow, 1) = RS.Fields("KNumber")
                tmp = Governance_Report_Get_EHS_Last_Performed(RS.Fields("KCode"), UserPeriod)
                wks.Cells(iRow, 3) = tmp
                If tmp = "N/A" Then
                    wks.Cells(iRow, 5) = "N/A"
                Else
                    wks.Cells(iRow, 5) = Governance_Report_Get_EHS_Issue_Count(RS.Fields("KCode"), UserPeriod)
                End If
                iRow = iRow + 1
            Case Else:
        End Select
        RS.MoveNext
    Loop
    If tmp_Has_C_Contract <> 1 Then
        Call Copy_Paste_Table_Block(wbk, wks, "EHS_1_B", iRow)
        wks.Cells(iRow, 1) = "No Construction Contract"
        iRow = iRow + 1
    End If
    RS.Close

    
    '*************************
    'Draw EHS TABLE 2
    '*************************
    iRow = iRow + TableGap
    Call Copy_Paste_Table_Block(wbk, wks, "EHS_2_H", iRow)
    iRow = iRow + 3
    
    sSQL = "SELECT * FROM [Qry_Governance_EHS_Incident] WHERE [P6ID] = " & UserProjectID & " AND [Period] = " & UserPeriod
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    Do Until RS.EOF
        Call Copy_Paste_Table_Block(wbk, wks, "EHS_2_B", iRow)
        wks.Cells(iRow, 1) = RS.Fields("KNumber")
        wks.Cells(iRow, 3) = RS.Fields("Incident Date")
        wks.Cells(iRow, 5) = RS.Fields("IType")
        wks.Cells(iRow, 7) = RS.Fields("IDesc")
        'Auto-Fit Merged Cell height
        wks.Cells(iRow, 7).Select
        AutoFitMergedCellRowHeight
        iRow = iRow + 1
        
        
        RS.MoveNext
    Loop
    If RS.RecordCount = 0 Then
        Call Copy_Paste_Table_Block(wbk, wks, "EHS_2_B", iRow)
        wks.Cells(iRow, 1) = "No Incident Recorded"
        iRow = iRow + 1
    End If
    RS.Close
    
    '*************************
    'Draw EHS TABLE 3
    '*************************
    iRow = iRow + TableGap
    Call Copy_Paste_Table_Block(wbk, wks, "EHS_3_H", iRow)
    
    '*************************
    'Draw Scope Table 1
    '*************************
    iRow = iRow + SectionGap + 2
    
    Call Copy_Paste_Table_Block(wbk, wks, "SCO_1_H", iRow)
    'Change table header by description
    If Governance_Report_Get_Design_Or_Construction_Phase(LastAchievedPhase) = "D" Then
        wks.Cells(iRow + 1, 1) = "Open Scope Changes to Baseline Scope > $100k"
    Else
        wks.Cells(iRow + 1, 1) = "Open NMSC Change Order Requests During Construction > $100k"
    End If
        
    '*************************
    'Draw Schedule Table 1
    '*************************
    iRow = iRow + SectionGap + 7
    
'Force Page Break
wks.Cells(iRow, 1).Select
ActiveWindow.SelectedSheets.HPageBreaks.Add Before:=ActiveCell
'leave space for stapling
'iRow = iRow + 3
    
    Call Copy_Paste_Table_Block(wbk, wks, "SCH_1_H", iRow)
    iRow = iRow + 4
    Call Copy_Paste_Table_Block(wbk, wks, "SCH_1_B", iRow)
        
    sSQL = "SELECT * FROM [Q_P6_Project_Schedule] WHERE [P6_Proj_id] = " & UserProjectID & " AND [Data_Period] = " & UserPeriod & " AND FB = 'F'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    
    Do Until RS.EOF
        LastAchievedPhase = RS.Fields("LastA")
        'LastPhaseMilestone = Governance_Report_Convert_Milestone_To_Phase_Milestone(RS.Fields("LastA"))
        
        wks.Cells(iRow, 1) = Governance_Report_Get_Phase(LastAchievedPhase, 0)
        wks.Cells(iRow, 4) = Governance_Report_Get_Phase(LastAchievedPhase, 1)
        'Select Case RS.Fields("Contract_Type")
        ScheduleDates = Governance_Report_Get_Schedule_Dates(UserProjectID, UserPeriod, LastAchievedPhase)
        Slippage = Governance_Report_Get_Schedule_Slippage(UserProjectID, UserPeriod, LastAchievedPhase)
        RS.MoveNext
    Loop
    RS.Close
    wks.Cells(iRow, 3) = ScheduleDates(0)
    wks.Cells(iRow, 6) = ScheduleDates(1)
    wks.Cells(iRow, 8) = ScheduleDates(2)
    wks.Cells(iRow, 13) = Slippage(0)
    wks.Cells(iRow, 15) = Slippage(1)
    
    '*************************
    'Draw Schedule Table 2
    '*************************
    iRow = iRow + TableGap + 1
    Call Copy_Paste_Table_Block(wbk, wks, "SCH_2_H", iRow)
    
    
    '*************************
    'Draw Budget Table 1 (only for construction)
    '*************************
    iRow = iRow + SectionGap + 3
    Call Copy_Paste_Table_Block(wbk, wks, "BUD_1_H", iRow)
    iRow = iRow + 4
    
    'sSQL = "SELECT * FROM [Q_P6_Contract_Company] WHERE P6ID = " & UserProjectID
    sSQL = "SELECT * FROM [Q_P6_Contract_Company] WHERE P6ID = " & UserProjectID & " AND [Project_ID] = '" & UserProjectName & "'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    Do Until RS.EOF
        'Select Case RS.Fields("Contract_Type")
            'Case "C":
                Call Copy_Paste_Table_Block(wbk, wks, "BUD_1_B", iRow)
                wks.Cells(iRow, 1) = RS.Fields("KNumber")
                wks.Cells(iRow, 2) = Governance_Report_Get_Budget_Baseline_Value(UserProjectName, RS.Fields("KNumber"))
                wks.Cells(iRow, 4) = Governance_Report_Get_Earned_Value(UserProjectName, RS.Fields("KNumber"))
                wks.Cells(iRow, 7) = Governance_Report_Get_Percent_Complete_By_KCode(RS.Fields("KCode"))
                wks.Cells(iRow, 8) = Governance_Report_Get_Budget_EAC(UserProjectName, RS.Fields("KNumber"))
                iRow = iRow + 1
            'Case Else:
        'End Select
        RS.MoveNext
    Loop
    If RS.RecordCount = 0 Then
        Call Copy_Paste_Table_Block(wbk, wks, "BUD_1_B", iRow)
        wks.Cells(iRow, 1) = "N/A"
        iRow = iRow + 1
    End If
    RS.Close
    
    '*************************
    'Draw Budget Table 2 (only for design)
    '*************************
    'iRow = iRow + TableGap
    'Call Copy_Paste_Table_Block(wbk, wks, "BUD_2_H", iRow)
    'If LastAchievedPhase <> "SC" And LastAchievedPhase <> "FC" And LastAchievedPhase <> "NTP" And LastAchievedPhase <> "100" Then
    '    Call Copy_Paste_Table_Block(wbk, wks, "BUD_2_B", iRow)
    '    wks.Cells(iRow, 2) = Governance_Report_Get_Budget_Baseline_Value(UserProjectName, RS.Fields("KNumber"))
    '    wks.Cells(iRow, 8) = Governance_Report_Get_Budget_EAC(UserProjectName, RS.Fields("KNumber"))
    '    iRow = iRow + 1
    'End If
    
    '*************************
    'Draw Budget Table 3
    '*************************
    iRow = iRow + TableGap
    Call Copy_Paste_Table_Block(wbk, wks, "BUD_3_H", iRow)
    
    '*************************
    'Draw Quality Table 1
    '*************************
    iRow = iRow + SectionGap + 3
    Call Copy_Paste_Table_Block(wbk, wks, "QUA_0_H", iRow)
    
    If LastAchievedPhase <> "SC" And LastAchievedPhase <> "FC" And LastAchievedPhase <> "NTP" And LastAchievedPhase <> "100" Then
        iRow = iRow + 1
        Call Cut_Paste_Table_Block(wbk, wks, "QUA_1_H", iRow)
        iRow = iRow + 4
        'For i = 1 To 6
            'Select Case LastAchievedPhase
                'Case "DS": Call Copy_Paste_Table_Block(wbk, wks, "QUA_1_B_30", iRow)
                'Case "30": Call Copy_Paste_Table_Block(wbk, wks, "QUA_1_B_60", iRow)
                'Case "60": Call Copy_Paste_Table_Block(wbk, wks, "QUA_1_B_90", iRow)
                'Case "90": Call Copy_Paste_Table_Block(wbk, wks, "QUA_1_B_100", iRow)
                'Case Else: Call Copy_Paste_Table_Block(wbk, wks, "QUA_1_B_FP", iRow)
            'End Select
            'iRow = iRow + 1
        'Next
        Call Copy_Paste_Table_Block(wbk, wks, "QUA_1_B", iRow)
        iRow = iRow + 23
    End If
    
    '*************************
    'Draw Quality Table 2
    '*************************
    iRow = iRow + TableGap
    Call Copy_Paste_Table_Block(wbk, wks, "QUA_2_H", iRow)
    iRow = iRow + 4

    sSQL = "SELECT * FROM [Qry_Governance_Report_Quality_Performance] WHERE [P6ID] = " & UserProjectID
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    Do Until RS.EOF
        Call Copy_Paste_Table_Block(wbk, wks, "QUA_2_B", iRow)
        wks.Cells(iRow, 1) = RS.Fields("KNumber")
        wks.Cells(iRow, 2) = RS.Fields("OrigAmount")
        wks.Cells(iRow, 4) = RS.Fields("Work Complete")
        wks.Cells(iRow, 5) = RS.Fields("CO_Type")
        wks.Cells(iRow, 6) = RS.Fields("134")
        wks.Cells(iRow, 7) = Val(RS.Fields("CostImpact"))
        wks.Cells(iRow, 9) = "[" & RS.Fields("CO#") & "] " & RS.Fields("COTitle")
        'Auto-Fit Merged Cell height
        wks.Cells(iRow, 9).Select
        AutoFitMergedCellRowHeight
        iRow = iRow + 1
        RS.MoveNext
    Loop
    If RS.RecordCount = 0 Then
        Call Copy_Paste_Table_Block(wbk, wks, "QUA_2_B", iRow)
        wks.Cells(iRow, 1) = "N/A"
        iRow = iRow + 1
    End If
    RS.Close

    '*************************
    'Draw Quality Table 3
    '*************************
    iRow = iRow + TableGap
    Call Copy_Paste_Table_Block(wbk, wks, "QUA_3_H", iRow)
    iRow = iRow + 3
    Call Copy_Paste_Table_Block(wbk, wks, "QUA_3_B", iRow)

    sSQL = "SELECT * FROM [Qry_Governance_Report_Quality_Performance_Sum] WHERE [P6ID] = " & UserProjectID
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    Do Until RS.EOF
        Call Copy_Paste_Table_Block(wbk, wks, "QUA_3_B", iRow)
        wks.Cells(iRow, 1) = RS.Fields("KNumber")
        wks.Cells(iRow, 2) = RS.Fields("OrigAmount")
        wks.Cells(iRow, 4) = RS.Fields("Work Complete")
        wks.Cells(iRow, 5) = Nz(RS.Fields("COValueTotal"), 0)
        'If Not IsNull(RS.Fields("Work Complete")) Then wks.Cells(iRow, 7) = RS.Fields("COValueTotal") / RS.Fields("OrigAmount") * RS.Fields("Work Complete")
        If Not IsNull(RS.Fields("Work Complete")) Then wks.Cells(iRow, 8) = Val(Nz(RS.Fields("DEALL"), 0)) / (RS.Fields("OrigAmount") * RS.Fields("Work Complete"))
        If Not IsNull(RS.Fields("Work Complete")) Then wks.Cells(iRow, 9) = Val(Nz(RS.Fields("DOALL"), 0)) / (RS.Fields("OrigAmount") * RS.Fields("Work Complete"))
        'Fill up N/A for non-contruction contract
        If RS.Fields("Contract_Type") <> "C" Then
            wks.Cells(iRow, 7) = "N/A"
            wks.Cells(iRow, 8) = "N/A"
            wks.Cells(iRow, 9) = "N/A"
        End If
        iRow = iRow + 2
        RS.MoveNext
    Loop
    If RS.RecordCount = 0 Then
        Call Copy_Paste_Table_Block(wbk, wks, "QUA_3_B", iRow)
        wks.Cells(iRow, 2) = "N/A"
    End If
    RS.Close
    
    '*************************
    'Draw Footer
    '*************************
    iRow = iRow + TableGap
    Call Copy_Paste_Table_Block(wbk, wks, "FOOTER", iRow)
    
    
    'delete template tab
    Sheets("template").Select
    ActiveWindow.SelectedSheets.Delete

    Cells(2, 1).Select
    wbk.Save
    wbk.Close True 'False
    appExcel.Application.DisplayAlerts = True 'enable alert
    appExcel.Quit
    
    DoCmd.Hourglass False
    Select Case [Form_FRM:Governance Report].FileNaming.Column(0)
        Case 0:
            tmp = ""
        Case 1:
            tmp = Governance_Report_Get_Personnel_From_P6(UserProjectID, "Accountable") & "_"
        Case 2:
            tmp = Governance_Report_Get_Personnel_From_P6(UserProjectID, "Portfolio") & "_"
    End Select
    'MsgBox ([Form_FRM:Governance Report].FileNaming.Column(0))
    
    NewName = UserPath & "\" & tmp & "Governance_Report_" & UserProjectName & "_" & UserPeriod & ".xls"
    Name sOutput As NewName
    
End Function


Function Governance_Report_Get_Personnel(PrjID, Mode As String)
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [Q_P6_Project_Personnel] WHERE P6_Proj_id = " & PrjID
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    If Mode = "Accountable" Then
        Governance_Report_Get_Personnel = RS.Fields("Accountable")
    Else
        Governance_Report_Get_Personnel = RS.Fields("Portfolio")
    End If
    RS.Close
End Function

Function Governance_Report_Get_Personnel_From_P6(PrjID, Mode As String)
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [dbo_P6_Historical_Trend] WHERE P6_Proj_id = " & PrjID & " ORDER BY Data_Period DESC"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    If Mode = "Accountable" Then
        Governance_Report_Get_Personnel_From_P6 = RS.Fields("Accountable_Construction_Manager")
    Else
        Governance_Report_Get_Personnel_From_P6 = RS.Fields("Portfolio_Manager")
    End If
    RS.Close
End Function

Function Governance_Report_Get_Current_Phase(PrjID, UserPeriod)
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [dbo_P6_Historical_Trend] WHERE [P6_Proj_id] = " & PrjID & " AND [Data_Period] = " & UserPeriod
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        Select Case RS.Fields("current_Stage")
            Case "P": Governance_Report_Get_Current_Phase = "Planning"
            Case "Des": Governance_Report_Get_Current_Phase = "Design"
            Case "Des30": Governance_Report_Get_Current_Phase = "30% Design"
            Case "Des60": Governance_Report_Get_Current_Phase = "60% Design"
            Case "Des90": Governance_Report_Get_Current_Phase = "90% Design"
            Case "Des100": Governance_Report_Get_Current_Phase = "100% Design"
            Case "ConPro": Governance_Report_Get_Current_Phase = "Construction Procurement"
            Case "Con": Governance_Report_Get_Current_Phase = "Construction"
            Case "SC": Governance_Report_Get_Current_Phase = "Substantial Completion"
            Case "FC": Governance_Report_Get_Current_Phase = "Final Completion"
            'Case "PM": Governance_Report_Get_Current_Phase = "Planning"
            Case Else:
                Governance_Report_Get_Current_Phase = RS.Fields("current_Stage")
        End Select
    RS.Close
End Function

'no use, remove
Function Governance_Report_Get_Contract_Company(PrjID)
    Dim RS As DAO.Recordset
    Dim tmp As String
    
    Set db = CurrentDb
    sSQL = "SELECT * FROM [Q_P6_Contract_Company] WHERE P6ID = " & PrjID
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    tSize = RS.RecordCount + 1
    'Dim tResult(1 To 10, 1 To 3) As String
    Dim tResult() As String
    Do Until RS.EOF
        Counter = Counter + 1
        tResult(1) = RS.Fields("KNumber")
        tResult(2) = RS.Fields("Contract_Type")
        tResult(3) = RS.Fields("Company")
    Loop
    RS.Close
    Governance_Report_Get_Contract_Company = tResult
End Function

Function Governance_Report_Get_Phase(Target, Mode As Integer) 'Mode: 0.Last Achieved 1.Next 2.Phase Name
    Select Case Target
        Case "FC": Governance_Report_Get_Phase = "Final Completion"
        Case "SC": Governance_Report_Get_Phase = "Construction NTP"
        Case "NTP": Governance_Report_Get_Phase = "Construction NTP"
        Case "100": Governance_Report_Get_Phase = "100% Design"
        Case "90": Governance_Report_Get_Phase = "Design Start"
        Case "60": Governance_Report_Get_Phase = "Design Start"
        Case "30": Governance_Report_Get_Phase = "Design Start"
        Case "DS": Governance_Report_Get_Phase = "Design Start"
        Case "NA": Governance_Report_Get_Phase = "N/A"
        Case Else:
    End Select
    If (Mode = 1) Then
        Select Case Target
            Case "FC": Governance_Report_Get_Phase = "N/A"
            Case "SC": Governance_Report_Get_Phase = "Final Completion"
            Case "NTP": Governance_Report_Get_Phase = "Final Completion"
            Case "100": Governance_Report_Get_Phase = "Construction NTP"
            Case "90": Governance_Report_Get_Phase = "100% Design"
            Case "60": Governance_Report_Get_Phase = "100% Design"
            Case "30": Governance_Report_Get_Phase = "100% Design"
            Case "DS": Governance_Report_Get_Phase = "100% Design"
            Case "NA": Governance_Report_Get_Phase = "Design Start"
            Case Else:
        End Select
    End If
    If (Mode = 2) Then
        Select Case Target
            Case "FC": Governance_Report_Get_Phase = "Final Completion"
            Case "SC": Governance_Report_Get_Phase = "Construction"
            Case "NTP": Governance_Report_Get_Phase = "Construction"
            Case "100": Governance_Report_Get_Phase = "Construction Procurement"
            Case "90": Governance_Report_Get_Phase = "Design"
            Case "60": Governance_Report_Get_Phase = "Design"
            Case "30": Governance_Report_Get_Phase = "Design"
            Case "DS": Governance_Report_Get_Phase = "Design"
            Case "NA": Governance_Report_Get_Phase = "Facility Planning"
            Case Else:
        End Select
    End If
    
End Function
'Phase milestones have only 4 (FP,Design,Procurement,Construction), while P6 has more
Function Governance_Report_Convert_Milestone_To_Phase_Milestone(Target)
    Select Case Target
        Case "FC": Governance_Report_Convert_Milestone_To_Phase_Milestone = "NTP"
        Case "SC": Governance_Report_Convert_Milestone_To_Phase_Milestone = "NTP"
        Case "NTP": Governance_Report_Convert_Milestone_To_Phase_Milestone = "NTP"
        Case "100": Governance_Report_Convert_Milestone_To_Phase_Milestone = "100"
        Case "90": Governance_Report_Convert_Milestone_To_Phase_Milestone = "DS"
        Case "60": Governance_Report_Convert_Milestone_To_Phase_Milestone = "DS"
        Case "30": Governance_Report_Convert_Milestone_To_Phase_Milestone = "DS"
        Case "DS": Governance_Report_Convert_Milestone_To_Phase_Milestone = "DS"
        Case "NA": Governance_Report_Convert_Milestone_To_Phase_Milestone = "NA"
        Case Else:
    End Select
End Function
Function Governance_Report_Get_Schedule_Dates(UserProjectID, UserPeriod, Target) '0: Last Phase Baseline 1:Current Phase BaseLine 2:Current Phase Forecast
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [Q_P6_Project_Schedule] WHERE [P6_Link_Proj_ID] = '" & UserProjectID & "' AND [Data_Period] = " & UserPeriod
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    Do Until RS.EOF
        Select Case Target
            Case "FC":
            Case "NTP", "SC":
                If RS.Fields("FB") = "B" Then
                    tmpA = RS.Fields("NTP")
                    tmpB = RS.Fields("Final_Completion")
                Else
                    tmpC = RS.Fields("Final_Completion")
                End If
            Case "100":
                If RS.Fields("FB") = "B" Then
                    tmpA = RS.Fields("Design_Finish")
                    tmpB = RS.Fields("NTP")
                Else
                    tmpC = RS.Fields("NTP")
                End If
            Case "DS", "30", "60", "90":
                If RS.Fields("FB") = "B" Then
                    tmpA = RS.Fields("Design_Start")
                    tmpB = RS.Fields("Design_Finish")
                Else
                    tmpC = RS.Fields("Design_Finish")
                End If
            Case "NA", "BODR":
                If RS.Fields("FB") = "B" Then
                    tmpA = RS.Fields("FP_Start")
                    tmpB = RS.Fields("Design_Start")
                Else
                    tmpC = RS.Fields("Design_Start")
                End If
            Case Else:
        End Select
        RS.MoveNext
    Loop
    Dim tmp(0 To 2)
    
    tmp(0) = tmpA
    tmp(1) = tmpB
    tmp(2) = tmpC
    Governance_Report_Get_Schedule_Dates = tmp
End Function

Function Governance_Report_Get_Schedule_Slippage(UserProjectID, UserPeriod, Target)
    If UserPeriod Mod 100 = 1 Then
        PeriodLast = (Fix(UserPeriod / 100) - 1) * 100 + 12
    Else
        PeriodLast = UserPeriod - 1
    End If
    
    If UserPeriod Mod 100 <= 6 Then
        PeriodSix = (UserPeriod + 6) - 100
    Else
        PeriodSix = UserPeriod - 6
    End If
    
    
    Current = Governance_Report_Get_Schedule_Dates(UserProjectID, UserPeriod, Target)
    LastMonth = Governance_Report_Get_Schedule_Dates(UserProjectID, PeriodLast, Target)
    SixMonth = Governance_Report_Get_Schedule_Dates(UserProjectID, PeriodSix, Target)
    Dim tmp(0 To 1)

    If Current(2) <> "" And LastMonth(2) <> "" Then tmp(0) = DateDiff("d", Current(2), LastMonth(2))
    If Current(2) <> "" And SixMonth(2) <> "" Then tmp(1) = DateDiff("d", Current(2), SixMonth(2))

    Governance_Report_Get_Schedule_Slippage = tmp
End Function

Function Governance_Report_Get_EHS_Last_Performed(KCode, Period)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [Qry_Governance_EHS_Inspection_LastPerformed] WHERE [KCode] = " & KCode & " AND [Period] = " & Period
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    If RS.RecordCount > 0 Then
        Governance_Report_Get_EHS_Last_Performed = RS.Fields("LastPerformed")
    Else
        Governance_Report_Get_EHS_Last_Performed = "N/A"
    End If
    RS.Close
End Function


Function Governance_Report_Get_EHS_Issue_Count(KCode, Period)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [Qry_Governance_EHS_Inspection] WHERE [KCode] = " & KCode & " AND [Period] = " & Period
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
    If RS.RecordCount > 0 Then
        Governance_Report_Get_EHS_Issue_Count = RS.Fields("IssueCount")
    Else
        Governance_Report_Get_EHS_Issue_Count = "0"
    End If
    RS.Close
End Function
Function Governance_Report_Get_Earned_Value(P6Name, KNumber)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT SUM(VALUE) AS TotalEarn FROM [_Cobra_Budget] WHERE Replace(Resource,' ','') = '" & Replace(KNumber, " ", "") & "' AND [Cost Set] = 'BCWP' AND WBS = '" & P6Name & "'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.RecordCount > 0 Then
            Governance_Report_Get_Earned_Value = RS.Fields("TotalEarn")
        Else
            Governance_Report_Get_Earned_Value = "N/A"
        End If
    RS.Close
End Function

Function Governance_Report_Get_Budget_Baseline_Value(P6Name, KNumber)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    'sSQL = "SELECT SUM(VALUE) AS TotalBase FROM [_Cobra_Budget] WHERE Replace(Resource,' ','') = '" & Replace(KNumber, " ", "") & "' AND [Cost Set] = 'Budget' AND WBS = '" & P6Name & "'"
    sSQL = "SELECT SUM(VALUE) AS TotalBase FROM [_Cobra_Budget] WHERE Replace(Resource,' ','') = '" & Replace(KNumber, " ", "") & "' AND [Cost Set] = 'Budget'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.RecordCount > 0 Then
            Governance_Report_Get_Budget_Baseline_Value = RS.Fields("TotalBase")
        Else
            Governance_Report_Get_Budget_Baseline_Value = "N/A"
        End If
    RS.Close
End Function

Function Governance_Report_Get_Budget_EAC(P6Name, KNumber)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    'sSQL = "SELECT SUM(VALUE) AS TotalEAC FROM [_Cobra_Budget] WHERE Replace(Resource,' ','') = '" & Replace(KNumber, " ", "") & "' AND [Cost Set] = 'EAC' AND WBS = '" & P6Name & "'"
    sSQL = "SELECT SUM(VALUE) AS TotalEAC FROM [_Cobra_Budget] WHERE Replace(Resource,' ','') = '" & Replace(KNumber, " ", "") & "' AND [Cost Set] = 'EAC' "
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.RecordCount > 0 Then
            Governance_Report_Get_Budget_EAC = RS.Fields("TotalEAC")
        Else
            Governance_Report_Get_Budget_EAC = "N/A"
        End If
    RS.Close
End Function

Function Governance_Report_Get_Percent_Complete_By_KCode(KCode)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [Q_Work_Complete_By_Contract] WHERE [KCode] = " & KCode
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.RecordCount > 0 Then
            Governance_Report_Get_Percent_Complete_By_KCode = RS.Fields("Work Complete")
        Else
            Governance_Report_Get_Percent_Complete_By_KCode = "N/A"
        End If
    RS.Close
End Function

Function Governance_Report_Get_Design_Or_Construction_Phase(LastAchievedPhase)
    If LastAchievedPhase <> "SC" And LastAchievedPhase <> "FC" And LastAchievedPhase <> "NTP" And LastAchievedPhase <> "100" Then
        Governance_Report_Get_Design_Or_Construction_Phase = "D"
    Else
        Governance_Report_Get_Design_Or_Construction_Phase = "C"
    End If
End Function


Sub AutoFitMergedCellRowHeight()
    Dim CurrentRowHeight As Single, MergedCellRgWidth As Single
    Dim CurrCell As Range
    Dim ActiveCellWidth As Single, PossNewRowHeight As Single
    If ActiveCell.MergeCells Then
       With ActiveCell.MergeArea
            If .Rows.Count = 1 And .WrapText = True Then
                'Application.ScreenUpdating = False
                CurrentRowHeight = .RowHeight
                ActiveCellWidth = ActiveCell.ColumnWidth
                For Each CurrCell In Selection
                    MergedCellRgWidth = CurrCell.ColumnWidth + MergedCellRgWidth
                Next
                .MergeCells = False
                .Cells(1).ColumnWidth = MergedCellRgWidth
                .EntireRow.AutoFit
                PossNewRowHeight = .RowHeight
                .Cells(1).ColumnWidth = ActiveCellWidth
                .MergeCells = True
                .RowHeight = IIf(CurrentRowHeight > PossNewRowHeight, _
                 CurrentRowHeight, PossNewRowHeight)
            End If
        End With
    End If
End Sub


Function Baseline_Revision_Approval_Get_Budget_Value(PrjName, PType, CType)
    Dim db As Database
    Dim RS As DAO.Recordset
    Set db = CurrentDb
    sSQL = "SELECT * FROM [Q_Cobra_Budget_Info] WHERE WBS = '" & PrjName & "' AND [PType] = " & PType & " AND [CType] = '" & CType & "'"
    Set RS = db.OpenRecordset(sSQL, dbOpenSnapshot)
        If RS.RecordCount > 0 Then
            Baseline_Revision_Approval_Get_Budget_Value = RS.Fields("Value")
        Else
            'Baseline_Revision_Approval_Get_Budget_Value = ""
        End If
    RS.Close
End Function













