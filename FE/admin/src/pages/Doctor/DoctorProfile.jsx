import React, { useContext, useEffect, useState } from 'react';
import { DoctorContext } from '../../context/DoctorContext';
import { AppContext } from '../../context/AppContext';
import { toast } from 'react-toastify';
import axios from 'axios';

const DoctorProfile = () => {
    const { dentistId, dToken, profileData, setProfileData, getProfileData } = useContext(DoctorContext);
    const { currency, backendUrl } = useContext(AppContext);
    const [isEdit, setIsEdit] = useState(false);

    const updateProfile = async () => {
        try {
            const updateData = {
                dentistId,
                available: profileData.isWorking ? 1 : 0, // Chuyển thành 1 (true) hoặc 0 (false)
                about: profileData.about,
                fees: profileData.fees,
            };
    
            const { data } = await axios.post(
                `${backendUrl}/api/doctor/update-profile`,
                updateData,
                { headers: { dToken } }
            );
    
            if (data.success) {
                toast.success(data.message);
                setIsEdit(false);
                getProfileData(); // Cập nhật lại dữ liệu
            } else {
                toast.error(data.message);
            }
        } catch (error) {
            toast.error(error.message);
            console.error(error);
        }
    };

    useEffect(() => {
        if (dToken) {
            getProfileData();
        }
    }, [dToken]);

    return profileData && (
        <div>
            <div className='flex flex-col gap-4 m-5'>
                <div>
                    <img className='bg-primary/80 w-full sm:max-w-64 rounded-lg' src={profileData.imgUrl} alt="" />
                </div>

                <div className='flex-1 border border-stone-100 rounded-lg p-8 py-7 bg-white'>
                    {/* ----- Doc Info : name, degree, experience ----- */}
                    <p className='flex items-center gap-2 text-3xl font-medium text-gray-700'>{profileData.name}</p>
                    <div className='flex items-center gap-2 mt-1 text-gray-600'>
                    <p>{profileData.speciality} - {profileData.position}</p>

                <button
                    onClick={() => {
                        if (isEdit) { // Allow toggling only when in edit mode
                        setProfileData(prev => ({
                            ...prev,
                            isWorking: !prev.isWorking
                        }));
                }
                }}
                    className={`py-0.5 px-2 border text-xs rounded-full ${profileData.isWorking ? 'bg-green-200' : 'bg-red-200'} ${isEdit ? '' : 'opacity-50 cursor-not-allowed'}`}
                    disabled={!isEdit} // Disable the button if not in edit mode
                >
                {profileData.isWorking ? 'Available' : 'Not Available'}
                </button>
                </div>


                    {/* ----- Doc About ----- */}
                    <div>
                        <p className='flex items-center gap-1 text-sm font-medium text-[#262626] mt-3'>About :</p>
                        <p className='text-sm text-gray-600 max-w-[700px] mt-1'>
                            {isEdit
                                ? <textarea
                                    onChange={(e) => setProfileData(prev => ({ ...prev, about: e.target.value }))}
                                    type='text'
                                    className='w-full outline-primary p-2'
                                    rows={8}
                                    value={profileData.about}
                                />
                                : profileData.about
                            }
                        </p>
                    </div>

                    <p className='text-gray-600 font-medium mt-4'>
                        Salary: $ <span className='text-gray-800'>
                             {profileData.fees}
                        </span>
                    </p>

                    <div className='text-gray-600 font-medium mt-4'>
                        <p>Email:</p>
                        <p className='text-sm'>{profileData.email}</p>
                    </div>

                    {
                        isEdit
                            ? <button
                                onClick={updateProfile}
                                className='px-4 py-1 border border-primary text-sm rounded-full mt-5 hover:bg-primary hover:text-white transition-all'
                              >
                                Save
                              </button>
                            : <button
                                onClick={() => setIsEdit(prev => !prev)}
                                className='px-4 py-1 border border-primary text-sm rounded-full mt-5 hover:bg-primary hover:text-white transition-all'
                              >
                                Edit
                              </button>
                    }
                </div>
            </div>
        </div>
    );
};

export default DoctorProfile;
