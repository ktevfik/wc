# Test counter
$passedTests = 0
$totalTests = 13

# Test file creation
$testContent = @"
Hello World
This is a test file
With multiple lines
And some numbers 12345
And special characters !@#$%
你好世界
Türkçe karakterler: çğıöşü
"@

$testFile = "test.txt"
$testContent | Out-File -FilePath $testFile -Encoding utf8

Write-Host "`nTesting file operations..."
Write-Host "------------------------"

# Test individual options with file
Write-Host "1. Testing -l (line count) with file:"
$result = .\target\wc-by-ktevfik.exe -l $testFile
if ($result -match "^\d+\s+test\.txt$") { $passedTests++ }

Write-Host "`n2. Testing -w (word count) with file:"
$result = .\target\wc-by-ktevfik.exe -w $testFile
if ($result -match "^\d+\s+test\.txt$") { $passedTests++ }

Write-Host "`n3. Testing -c (byte count) with file:"
$result = .\target\wc-by-ktevfik.exe -c $testFile
if ($result -match "^\d+\s+test\.txt$") { $passedTests++ }

Write-Host "`n4. Testing -m (character count) with file:"
$result = .\target\wc-by-ktevfik.exe -m $testFile
if ($result -match "^\d+\s+test\.txt$") { $passedTests++ }

Write-Host "`n5. Testing default (all counts) with file:"
$result = .\target\wc-by-ktevfik.exe $testFile
if ($result -match "^\d+\s+\d+\s+\d+\s+test\.txt$") { $passedTests++ }

Write-Host "`nTesting stdin operations..."
Write-Host "------------------------"

# Test individual options with stdin
Write-Host "6. Testing -l (line count) with stdin:"
$result = Get-Content $testFile | .\target\wc-by-ktevfik.exe -l
if ($result -match "^\d+$") { $passedTests++ }

Write-Host "`n7. Testing -w (word count) with stdin:"
$result = Get-Content $testFile | .\target\wc-by-ktevfik.exe -w
if ($result -match "^\d+$") { $passedTests++ }

Write-Host "`n8. Testing -c (byte count) with stdin:"
$result = Get-Content $testFile | .\target\wc-by-ktevfik.exe -c
if ($result -match "^\d+$") { $passedTests++ }

Write-Host "`n9. Testing -m (character count) with stdin:"
$result = Get-Content $testFile | .\target\wc-by-ktevfik.exe -m
if ($result -match "^\d+$") { $passedTests++ }

Write-Host "`n10. Testing default (all counts) with stdin:"
$result = Get-Content $testFile | .\target\wc-by-ktevfik.exe
if ($result -match "^\d+\s+\d+\s+\d+$") { $passedTests++ }

Write-Host "`nTesting error cases..."
Write-Host "------------------------"

Write-Host "11. Testing non-existent file:"
$result = .\target\wc-by-ktevfik.exe -l nonexistent.txt
if ($result -match "^Error: Cannot access file") { $passedTests++ }

Write-Host "`n12. Testing no input for options:"
$result = .\target\wc-by-ktevfik.exe -l
if ($result -eq "Error: No input provided") { $passedTests++ }

Write-Host "`n13. Testing invalid option:"
$result = .\target\wc-by-ktevfik.exe -x
if ($result -match "^Unknown command:") { $passedTests++ }

# Cleanup
Remove-Item $testFile

# Final report
Write-Host "`n------------------------"
Write-Host "Test Results: $passedTests/$totalTests tests passed"
if ($passedTests -eq $totalTests) {
    Write-Host "All tests passed!" -ForegroundColor Green
} else {
    Write-Host "Some tests failed!" -ForegroundColor Red
} 