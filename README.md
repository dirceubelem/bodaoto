# BODAOTO

https://jitpack.io/private#dirceubelem/bodaoto/1.1

[![](https://www.jitpack.io/v/dirceubelem/bodaoto.svg)](https://www.jitpack.io/#dirceubelem/bodaoto)

Framework para trabalhar classes DAO e TO de forma simples e eficiente.

## Como funciona

#### [Classes TO]

Vamos supor uma tabela no banco de dados com o nome phonenumber a partir do seguinte script:

```

create table phonenumber
(
    areacode    varchar(2)              not null,
    phonenumber varchar(9)              not null,
    idcarrier   integer                 not null,
    document    varchar(15)             not null,
    createat    timestamp default now() not null,
    constraint pk_phonenumber
        primary key (areacode, phonenumber, document)
);

```

A partir dessa tabela podemos criar o seguinte TO:

```

@Table(name = "phonenumber")
public class TOPhoneNumber extends TOBase {

    @Column(name = "document", jsonName = "document", type = Column.TYPE.KEY)
    private String document;

    @Column(name = "idcarrier", jsonName = "idCarrier")
    private int idCarrier;

    @Column(name = "areacode", jsonName = "areaCode")
    private String areaCode;

    @Column(name = "phonenumber", jsonName = "phoneNumber", mainOrder = true)
    private String phoneNumber;

    @Column(name = "createat", jsonName = "createAt", dateTime = true, dateTimeFormat = "dd/MM/yyyy HH:mm")
    private Timestamp createAt;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public int getIdCarrier() {
        return idCarrier;
    }

    public void setIdCarrier(int idCarrier) {
        this.idCarrier = idCarrier;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}

```

Com isso todo o trabalho de insert, update, delete, list e get ficam da seguinte forma:

## Criação do Objeto normalmente

```

TOPhoneNumber t = new TOPhoneNumber();
t.setDocument("00000000000");
t.setIdCarrier(1);
t.setAreaCode("11");
t.setPhoneNumber("999999999");
t.setCreateAt((new DateTime("2021-02-26", "yyyy-MM-dd")).getTimestamp());

```

## Insert

```
BOFactory.insert(c, t);
```

## Update

```
BOFactory.update(c, t);
```

## Delete

```
BOFactory.delete(c, t);
```

## Get

```
TOPhoneNumber t = (TOPhoneNumber)BOFactory.get(c, t);
```

## List

```
JSONArray a = BOFactory.list(c, t);
```

## List - Paginada

```
JSONArray a = BOFactory.list(c, t, "1199999999", 50, 5);
```

## Dependencies

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
    implementation 'com.github.dirceubelem:bodaoto:1.2'
}
```

## License

```
Copyright 2021 Dirceu Belém

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
