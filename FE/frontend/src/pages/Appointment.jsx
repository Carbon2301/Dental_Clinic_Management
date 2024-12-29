import React, { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { AppContext } from '../context/AppContext';
import { assets } from '../assets/assets';
import RelatedDoctors from '../components/RelatedDoctors';
import axios from 'axios';
import { toast } from 'react-toastify';

const Appointment = () => {
    const { dentistId } = useParams();
    const { currencySymbol, backendUrl, token, getDoctorsData, userId } = useContext(AppContext);
    const daysOfWeek = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];

    const [dentist, setDentist] = useState(null);
    const [docSlotLength, setdocSlotLength] = useState();
    const [docSlots, setDocSlots] = useState([]);
    const [slotIndex, setSlotIndex] = useState(0);
    const [slotTime, setSlotTime] = useState('');

    const navigate = useNavigate();

    // Fetch dentist details from backend
    const fetchDentistDetails = async () => {
        try {
            const { data } = await axios.get(`${backendUrl}/api/doctor/profile?dentistId=${dentistId}`);
            if (data.success) {
                setDentist(data.profileData);
            } else {
                toast.error(data.message);
            }
        } catch (error) {
            console.error(error);
            toast.error('Failed to fetch dentist details');
        }
    };

    // Fetch available slots for the dentist
    const getAvailableSlots = async () => {
        try {
            const { data } = await axios.get(`${backendUrl}/api/user/${dentistId}/available-slots`);
            if (data.success) {
                setDocSlots(data.data);
                setdocSlotLength(data.docSlotLength)
            } else {
                toast.error(data.message);
            }
        } catch (error) {
            console.error(error);
            toast.error('Failed to fetch available slots');
        }
    };

    // Book an appointment
    const bookAppointment = async () => {
        if (!token) {
            toast.warning('Login to book an appointment');
            return navigate('/login');
        }

        const selectedSlot = docSlots[slotIndex]?.date;
        console.log(selectedSlot);
        if (!selectedSlot || !slotTime) {
            toast.warning('Please select a valid slot');
            return;
        }

        const date = new Date(selectedSlot);
        const slotDate = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;

        try {
            const { data } = await axios.post(
                `${backendUrl}/api/user/book-appointment`,
                { dentistId, slotDate, slotTime, userId },
                { headers: { token } }
            );

            if (data.success) {
                toast.success(data.message);
                getDoctorsData();
                navigate('/my-appointments');
            } else {
                toast.error(data.message);
            }
        } catch (error) {
            console.error(error);
            toast.error('Failed to book appointment');
        }
    };

    useEffect(() => {
        fetchDentistDetails();
    }, [dentistId]);

    useEffect(() => {
        if (dentist) {
            getAvailableSlots();
        }
    }, [dentist]);

    return dentist ? (
        <div>
            {/* Doctor Details */}
            <div className='flex flex-col sm:flex-row gap-4'>
                <div>
                    <img className='bg-primary w-full sm:max-w-72 rounded-lg' src={dentist.imgUrl} alt='' />
                </div>
                <div className='flex-1 border border-[#ADADAD] rounded-lg p-8 py-7 bg-white mx-2 sm:mx-0 mt-[-80px] sm:mt-0'>
                    <p className='flex items-center gap-2 text-3xl font-medium text-gray-700'>{dentist.name} <img className='w-5' src={assets.verified_icon} alt='' /></p>
                    <div className='flex items-center gap-2 mt-1 text-gray-600'>
                        <p>{dentist.speciality}</p>
                        <button className='py-0.5 px-2 border text-xs rounded-full'>5 Years</button>
                    </div>
                    <div>
                        <p className='flex items-center gap-1 text-sm font-medium text-[#262626] mt-3'>About <img className='w-3' src={assets.info_icon} alt='' /></p>
                        <p className='text-sm text-gray-600 max-w-[700px] mt-1'>{dentist.about}</p>
                    </div>
                    <p className='text-gray-600 font-medium mt-4'>Appointment fee: <span className='text-gray-800'>{currencySymbol}{100}</span></p>
                </div>
            </div>

            {/* Booking Slots */}
            <div className='flex gap-3 items-center w-full overflow-x-scroll mt-4'>
            {docSlots.map((item, index) => (
                <div
                    onClick={() => setSlotIndex(index)}
                    key={index}
                    className={`text-center py-6 min-w-16 rounded-full cursor-pointer ${slotIndex === index ? 'bg-primary text-white' : 'border border-[#DDDDDD]'}`}
                >
                    <p>{daysOfWeek[new Date(item.date).getDay()]}</p>
                    <p>{new Date(item.date).getDate()}</p>
                </div>
            ))}
            </div>
            <div className='flex items-center gap-3 w-full overflow-x-scroll mt-4'>
                {docSlots[slotIndex]?.slots.map((slot, index) => (
                    <p
                        onClick={() => setSlotTime(slot)}
                        key={index}
                        className={`text-sm font-light flex-shrink-0 px-5 py-2 rounded-full cursor-pointer ${slot === slotTime ? 'bg-primary text-white' : 'text-[#949494] border border-[#B4B4B4]'}`}
                    >
                        {slot.toLowerCase()}
                    </p>
                ))}
            </div>

            <button
                onClick={bookAppointment}
                className='bg-primary text-white text-sm font-light px-20 py-3 rounded-full my-6'
            >
            Book an appointment
            </button>

            {/* Listing Related Doctors */}
            <RelatedDoctors speciality={dentist.speciality} dentistId={dentistId} />
        </div>
    ) : null;
};

export default Appointment;
