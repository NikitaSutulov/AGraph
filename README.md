# AGraph
AGraph – це програма для Android, яка дозволяє створювати, редагувати та зберігати лінійні графіки, які використовують лінії для з’єднання окремих точок даних. Програма має на меті надати користувачам інтуїтивно зрозумілий інтерфейс і надійну функціональність для легкого створення лінійних графіків професійного рівня для різних цілей, таких як аналіз даних, візуалізація та презентації. Пропонуючи зручний досвід у поєднанні з потужними можливостями побудови графіків, програма прагне задовольнити широке коло користувачів, включаючи студентів, дослідників, аналітиків і професіоналів.

Для роботи з цим проєктом у вас має бути встановлений AndroidSDK та Android Studio, а також gradle.

Для того, щоб зібрати проєкт, в терміналі напишіть наступну команду:
```
$ ./gradlew build
```
Щоб запустити юніт-тести, в терміналі напишіть наступну команду:
```
$ ./gradlew test
```
Щоб запустити UI-тести, в терміналі напишіть наступну команду:
```
$ ./gradlew connectedAndroidTest
```
Також ви можете зібрати apk-файл за допомогою команди:
```
$ ./gradlew assembleDebug
```
Цей apk буде знаходитися в каталозі app/build/outputs/apk/, і його вже можна передати на мобільний девайс або емулятор та встановити туди.

Або ж якщо потрібно одразу зібрати та встановити додаток на емулятор:
```
$ ./gradlew installDebug
```
Детально з дизайн-документом цього проєкту можна ознайомитися за [цим посиланням](https://docs.google.com/document/d/169SeufuUD9Q-0isuVccKdIaLiOtNQs1sj64bZP-lbZc).

Пул реквести, на які код рев'ю зроблено мною:
- https://github.com/michigang1/healthcare-app/pull/2
- https://github.com/michigang1/healthcare-app/pull/3
- https://github.com/michigang1/healthcare-app/pull/4
- https://github.com/michigang1/healthcare-app/pull/5

Пул реквести, на які рев'ю зробили мені:
- https://github.com/NikitaSutulov/AGraph/pull/1
- https://github.com/NikitaSutulov/AGraph/pull/2
- https://github.com/NikitaSutulov/AGraph/pull/3
- https://github.com/NikitaSutulov/AGraph/pull/4
