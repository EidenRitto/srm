�}ǩ
 < ����-�y��qw
��1�:�G�	�ֱ�F�uWMX4@a�b��=�;K��C�=�#��b�a�l ���LA���jj����?�D��kz��՚5���jF�h��?I=�?��zo���K����,��K�].���$6��"j�dOAٖ+K. �W:�,K��g�j���+���FVe)!;�d�)�Y�:���䬃&'�\G�כ��Ɖj-���~���P9E�5��*�Ӷ��X�S)*3|<���L����?@����%(L~� �+0�,ac�)�H"��%���Ƹ|YiH��������+ۛu F�[�1m
W�M��y���
�ؕ�a+����;�A�."h�p	�����A�SR$3�n�!ʋi������=sh��y�h���Ԑ�!�ZR�>G�X���O͏!ኌ��E{��P(�ʰ?@�,C� G Q_(�i��f�ay����&&[����vKCƱ-pk:� �r+�!��::�)9�a�7@������9�>ublic long count();

    @Query(value = "select * from sync s order by id limit (s.page=:page)-1*(s.limit=:limit),s.limit=:limit",nativeQuery = true)
    public List findAllPage(@Param("page") int page, @Param("limit")int limit);
}
