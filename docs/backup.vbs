Dim WshShell
set WshShell = CreateObject("wscript.Shell")
intButton = WshShell.Popup ("Nu een backup maken ? Zoja; Klik op OK tot
je een scheppend mannetje ziet", 0, "Wekelijkse Backup", 36)

Function ReportDriveStatus(drv)
    Dim fso, msg
    Set fso = CreateObject("Scripting.FileSystemObject")
    If fso.DriveExists(drv) Then
       msg = ("exists")
    Else
       msg = ("doesn't exist")
    End If
    ReportDriveStatus = msg
End Function


if (intButton = 6) then

if (ReportDriveStatus("X")="doesn't exist") Then
WshShell.Popup "Koppel de schijf X:\ binnen 1 minuut, wanneer dit niet
gebeurd komt de melding volgende week gewoon weer op, geen probleem.
(klik OK als je klaar bent)" & _
                " ", 0, "Automatische Detectie...", 48


End If

timespan = datediff("s",#2014/1/1#,now()) + 60

Do While (ReportDriveStatus("X")="doesn't exist")

     If  timespan = datediff("s",#2014/1/1#,now()) Then
     WshShell.Popup "Melding verzet, geen probleem. (klik OK)" & _
                " ", 0, "Schijf niet op tijd gevonden", 48
     WScript.Quit
     End If

     WScript.Sleep 2000

Loop

WshShell.Popup "Schijf gedetecteerd, ik geef de schijf even 10 seconden,
daarna start het backupprogramma automatisch (klik OK om te werken)" & _
                " ", 0, "Ik ben dr Klaar voor!", 48
WScript.Sleep 10000 'give drive some time to bootup
WshShell.Run "AthenaSync.ffs_batch"


End if
