Feature:

  @DBrole
  Scenario:
    Given database baglantisi kurulur
    Then "become_instructors" tablosusunun "role" sutununda "teacher" ve "organization" rolleri dışında bir şey olmadigi dogrulanir
    And database baglantisi kapatilir