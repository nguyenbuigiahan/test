git init
git add README.md
git commit -m "first commit"
for %%* in (.) do set CurrDirName=%%~nx*
git remote add origin https://github.com/shellsh1/%CurrDirName%.git
git push -u origin master
PAUSE