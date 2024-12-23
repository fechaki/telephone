insert into tbl_telephone
    (telephone_id, country_code, area_code, phone_number, activated, validated, deleted, created, updated)
values
    ('6031dff8-2cd4-4dc7-b3b7-0d0a8b1d4ee8', '55', '21', '999999999', false, false, false, current_timestamp, current_timestamp);

insert into tbl_telephone
    (telephone_id, country_code, area_code, phone_number, activated, validated, deleted, created, updated)
values
    ('018b2f19-e79e-7d6a-a56d-29feb6211b04', '55', '21', '988888888', false, false, false, current_timestamp, current_timestamp);

insert into tbl_telephone
    (telephone_id, country_code, area_code, phone_number, activated, validated, deleted, created, updated)
values
    ('afebdfe0-3ccf-4b3a-a42c-f5d8c8bf0af7', '55', '21', '977777777', false, false, false, current_timestamp, current_timestamp);

insert into tbl_telephone
    (telephone_id, country_code, area_code, phone_number, activated, validated, deleted, created, updated)
values
    (gen_random_uuid(), '55', '21', '966666666', false, false, false, current_timestamp, current_timestamp);
