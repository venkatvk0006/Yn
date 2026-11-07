# APK உருவாக்குவது (தமிழில்)

இந்த project-இல் ஏற்கெனவே இருந்த `app-debug.apk` கோப்பு சரியான APK ZIP வடிவில் இல்லை. அதனால் source code-இலிருந்து புதிய APK உருவாக்க GitHub Actions workflow சேர்க்கப்பட்டுள்ளது.

## GitHub மூலம் APK பெறுவது
1. இந்த ZIP-ஐ extract செய்யவும்.
2. GitHub-ல் புதிய repository உருவாக்கவும்.
3. Extract செய்த project-இன் எல்லா கோப்புகளையும் repository-க்கு upload செய்து commit செய்யவும்.
4. Repository-ல் **Actions** tab திறக்கவும்.
5. **Build Android APK** workflow-ஐ தேர்ந்தெடுத்து **Run workflow** அழுத்தவும். (main/master branch-க்கு push செய்தாலும் build தானாகத் தொடங்கும்.)
6. Build வெற்றியடைந்ததும் அந்த workflow run-ஐ திறக்கவும்.
7. கீழே உள்ள **Artifacts** பகுதியில் `hyperos-mtz-theme-installer-debug-apk` என்பதை download செய்யவும்.
8. ZIP-ஐ extract செய்து `app-debug.apk`-ஐ Android போனில் நிறுவவும்.

## குறிப்பு
- இது debug APK; Play Store release signing அல்ல.
- Android 7.0 (API 24) அல்லது அதற்கு மேல் தேவைப்படுமாறு project அமைக்கப்பட்டுள்ளது.
- Build தோல்வியடைந்தால் Actions-இல் உள்ள error log-ஐ screenshot ஆக அனுப்பவும்.
